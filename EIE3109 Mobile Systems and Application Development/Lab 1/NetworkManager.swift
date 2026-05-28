//Li Ming Chun Simon 25017659D
import Foundation

class NetworkManager {
    // Hugging Face API Configuration
    private let apiKey = "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" // Get from huggingface.co/settings/tokens
    private let baseURL = "https://router.huggingface.co/hf-inference"
    
    // Model endpoints - choose one
    private enum ModelEndpoint: String {
        // For specific language pairs
        case englishToFrench = "Helsinki-NLP/opus-mt-en-fr"
        case englishToSpanish = "Helsinki-NLP/opus-mt-en-es"
        case englishToGerman = "Helsinki-NLP/opus-mt-en-de"
        case englishToChinese = "Helsinki-NLP/opus-mt-en-zh"
        case englishToJapanese = "Helsinki-NLP/opus-mt-en-jap"
        
        //exercise 1 (Add Korean and Hindi)
        case englishToKorean = "Helsinki-NLP/opus-mt-en-ko"
        case koreanToEnglish = "Helsinki-NLP/opus-mt-ko-en"
        case englishToHindi = "Helsinki-NLP/opus-mt-en-hi"
        case hindiToEnglish = "Helsinki-NLP/opus-mt-hi-en"
        
        //Multi-language models (Exercise 1)
        case englishToMulti = "Helsinki-NLP/opus-mt-en-mul"
        case multiToEnglish = "Helsinki-NLP/opus-mt-mul-en"
        
        // Other models
        case t5Small = "google-t5/t5-small"    //German
        case m2m100 = "facebook/m2m100_418M"   //need extra implementation to use the facebook models
        case mbart = "facebook/mbart-large-50-many-to-many-mmt"
    }
    
    private var currentModel: ModelEndpoint = .englishToSpanish
    
    func translate(text: String, sourceLang: String, targetLang: String) async throws -> String {
        guard !text.isEmpty else { return "" }
        
        // Exercise 2: non-English to non-English via English
        if sourceLang != "en" && targetLang != "en" {
            // Step 1: source -> English
            let englishText = try await translate(
                text: text,
                sourceLang: sourceLang,
                targetLang: "en"
            )
            
            // Step 2: English -> target
            return try await translate(
                text: englishText,
                sourceLang: "en",
                targetLang: targetLang
            )
        }
        
        // Select appropriate model based on language pair
        let model = selectModel(for: sourceLang, to: targetLang)
        
        // Consturct the URL and configure the URL Request
        guard let url = URL(string: "\(baseURL)/models/\(model.rawValue)") else {
            throw NetworkError.invalidResponse
        }
        
        // Configure URL Request
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.addValue("Bearer \(apiKey)", forHTTPHeaderField: "Authorization")
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")
        
        
        var requestBody: [String: Any] = [:]
        if model.rawValue == ModelEndpoint.t5Small.rawValue {
            // Create the proper prompt for T5
            let prompt = createT5Prompt(text: text, sourceLang: sourceLang, targetLang: targetLang)
            requestBody = [
                "inputs": prompt,
                "parameters": [
                    "max_length": 200,
                    "num_return_sequences": 1,
                    "temperature": 0.7,
                    "do_sample": true
                ],
                "options": [
                    "wait_for_model": true,
                    "use_cache": false
                ]
            ]
        } else {
            // Request body format for Hugging Face inference API
            var inputText = text
            
            if model == .englishToMulti {
                let targetISO3 = getISO3Code(targetLang)
                inputText = ">>\(targetISO3)<< \(text)"
            }
            
            if model == .m2m100 {
                requestBody = [
                    "inputs": text,
                    "parameters": [
                        "src_lang": sourceLang,
                        "tgt_lang": targetLang,
                        "max_length": 200
                    ],
                    "options": [
                        "wait_for_model": true
                    ]
                ]
            } else {
                requestBody = [
                    "inputs": inputText,
                    "parameters": [
                        "max_length": 200,
                        "num_return_sequences": 1
                    ],
                    "options": [
                        "wait_for_model": true,
                        "use_cache": false
                    ]
                ]
            }
        }
        
        request.httpBody = try JSONSerialization.data(withJSONObject: requestBody)
        
        // Make the request
        let (data, response) = try await URLSession.shared.data(for: request)
        
        guard let httpResponse = response as? HTTPURLResponse else {
            throw NetworkError.invalidResponse
        }
        
        // Check for model loading
        if httpResponse.statusCode == 503 {
            // Model is loading, wait and retry
            try await Task.sleep(nanoseconds: 5_000_000_000) // 5 seconds
            return try await translate(text: text, sourceLang: sourceLang, targetLang: targetLang)
        }
        
        guard httpResponse.statusCode == 200 else {
            print("HTTP Error: \(httpResponse.statusCode)")
            print("Response: \(String(data: data, encoding: .utf8) ?? "")")
            throw NetworkError.serverError(httpResponse.statusCode)
        }
        
        // Parse response based on model type
        if model.rawValue == ModelEndpoint.t5Small.rawValue {
            return try parseT5Response(data: data)
        } else {
            return try parseTranslationResponse(data: data, model: model)
        }
        
    }
    
    private func selectModel(for sourceLang: String, to targetLang: String) -> ModelEndpoint {
        if sourceLang == "en" && targetLang == "ko" { 
            return .englishToKorean }
        if sourceLang == "ko" && targetLang == "en" { 
            return .koreanToEnglish }
        if sourceLang == "en" && targetLang == "hi" { 
            return .englishToHindi }
        if sourceLang == "hi" && targetLang == "en" { 
            return .hindiToEnglish }
        
        if sourceLang == "en" {
            switch targetLang {
            case "fr": return .englishToFrench
            case "es": return .englishToSpanish
            case "de": return .englishToGerman
            case "zh": return .englishToChinese
            case "ja": return .englishToJapanese
            default: return .englishToMulti
            }
        }
        
        if targetLang == "en" {
            return .multiToEnglish
        }
        
        // Fallback for unsupported specific pairs
        return .t5Small
    }
    
    private func parseTranslationResponse(data: Data, model: ModelEndpoint) throws -> String {
        // Try different response formats
        
        // Format 1: Direct translation text
        if let responseString = String(data: data, encoding: .utf8) {
            if let json = try? JSONSerialization.jsonObject(with: data) as? [[String: Any]],
               let first = json.first,
               let translationText = first["translation_text"] as? String {
                return translationText
            }
            
            // Format 2: For T5 and similar models
            //Should not reach this if, T5 model should have used func parseT5Response(data: Data) already
            if let json = try? JSONSerialization.jsonObject(with: data) as? [[String: Any]],
               let first = json.first,
               let generatedText = first["generated_text"] as? String {
                return generatedText
            }
            
            // Format 3: Try to extract any text
            let cleaned = responseString
                .replacingOccurrences(of: "[\\[\\]\",]", with: "", options: .regularExpression)
                .trimmingCharacters(in: .whitespacesAndNewlines)
            
            if !cleaned.isEmpty && cleaned != "[]" {
                return cleaned
            }
        }
        
        throw NetworkError.noTranslation
    }
    
    //Exercise 1
    private func getISO3Code(_ code: String) -> String {
        let mapping: [String: String] = [
            "fr": "fra", "es": "spa", "de": "deu", "it": "ita",
            "pt": "por", "nl": "nld", "pl": "pol", "ru": "rus",
            "zh": "cmn", "ja": "jpn", "ko": "kor", "ar": "ara",
            "hi": "hin", "tr": "tur", "sv": "swe", "da": "dan",
            "fi": "fin", "no": "nor", "he": "heb", "el": "ell"
        ]
        return mapping[code] ?? code
    }
    
    private func createT5Prompt(text: String, sourceLang: String, targetLang: String) -> String {
        // Map language codes to T5 language names
        let sourceLangName = languageNameForT5(code: sourceLang)
        let targetLangName = languageNameForT5(code: targetLang)
        
        // T5 needs specific prompt format
        return "translate \(sourceLangName) to \(targetLangName): \(text)"
    }
    
    private func languageNameForT5(code: String) -> String {
        let languageMap: [String: String] = [
            "en": "English",
            "es": "Spanish",
            "fr": "French",
            "de": "German",
            "zh": "Chinese",
            "it": "Italian",
            "pt": "Portuguese",
            "ja": "Japanese",
            "ko": "Korean",
            "ru": "Russian",
            "ar": "Arabic",
            "hi": "Hindi",
            "nl": "Dutch",
            "pl": "Polish",
            "sv": "Swedish",
            "da": "Danish",
            "fi": "Finnish",
            "no": "Norwegian",
            "he": "Hebrew",
            "el": "Greek",
            "tr": "Turkish"
        ]
        return languageMap[code] ?? "English"
    }
    
    private func parseT5Response(data: Data) throws -> String {
        do {
            // T5 returns an array of dictionaries
            if let jsonArray = try JSONSerialization.jsonObject(with: data) as? [[String: Any]],
               let firstItem = jsonArray.first,
               let generatedText = firstItem["generated_text"] as? String {
                // Clean up the response
                return cleanT5Response(generatedText)
            }
            
            // Try alternative parsing
            if let responseString = String(data: data, encoding: .utf8) {
                print("Raw response: \(responseString)")
            }
            
        } catch {
            print("JSON parsing error: \(error)")
        }
        
        throw NetworkError.noTranslation
    }
    
    private func cleanT5Response(_ text: String) -> String {
        // Remove the prompt if it's included in the response
        var cleaned = text
        if let range = cleaned.range(of: ": ") {
            cleaned = String(cleaned[range.upperBound...])
        }
        
        // Clean up any extra quotes or brackets
        cleaned = cleaned
            .replacingOccurrences(of: "\"", with: "")
            .replacingOccurrences(of: "\\", with: "")
            .trimmingCharacters(in: .whitespacesAndNewlines)
        
        return cleaned
    }
}

// MARK: - Response Models
struct HuggingFaceTranslation: Decodable {
    let translation_text: String
}

// MARK: - Error Handling
enum NetworkError: Error, LocalizedError {
    case invalidResponse
    case serverError(Int)
    case modelLoading
    case noTranslation
    
    var errorDescription: String? {
        switch self {
        case .invalidResponse:
            return "Invalid response from server"
        case .serverError(let code):
            return "Server error: \(code)"
        case .modelLoading:
            return "Model is loading, please try again in a few seconds"
        case .noTranslation:
            return "No translation returned"
        }
    }
}
