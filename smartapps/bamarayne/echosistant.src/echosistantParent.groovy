/**
 *  Echosistant - Manager
 *		
 		10/29/2016	Version 0.0.1a		Security Tokens display in app and in logs, as well as the copyright.
        10/28/2016	Version 0.0.1		Initial File
 *
 *  Copyright 2016 Jason Headley
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name		: "Echosistant",
    namespace	: "sb",
    author		: "Jason Headley",
    description	: "The parent app.",
    category	: "My Apps",
    iconUrl		: "https://raw.githubusercontent.com/BamaRayne/alexaspeaks.src/master/app-Echosistant.png",
    iconX2Url	: "https://raw.githubusercontent.com/BamaRayne/alexaspeaks.src/master/app-Echosistant@2x.png",
    iconX3Url	: "https://raw.githubusercontent.com/BamaRayne/alexaspeaks.src/master/app-Echosistant@2x.png")
preferences {
	page name: "main"
    page name: "About"
    page name: "Reset"
    page name: "Confirmation"
}
def installed() {
log.debug "Installed with settings: ${settings}"
initialize()
log.debug "there are ${childApps.size()} child smartapps"
    childApps.each {child ->
        log.debug "child app: ${child.label}"
	}
}    
def updated() {
log.debug "Updated with settings: ${settings}"
initialize()
unsubscribe()
}
def initialize() {
	if (!state.accessToken) {
		log.error "Access token not defined. Ensure OAuth is enabled in the SmartThings IDE."
	}
}    
def main(){
	def installed = app.installationState == "COMPLETE"
	return dynamicPage(
    	name		: "main"
        ,title		: "Profiles"
        ,install	: true
        ,uninstall	: installed
        ){	
            if (installed){
        		section(){
        			app(name: "Profiles", appName: "echosistantProfile", namespace: "sb", description: "Create New Profile...", multiple: true)	
            }
               section("Security Tokens"){
                	paragraph ("Log into the IDE on your computer and navigate to the Live Logs tab. Leave that window open, come back here and open this section")
                    input "ShowTokens", "bool", title: "Show Security Tokens", default: false, submitOnChange: true
                    def msg = state.accessToken  
            		if (ShowTokens) paragraph "The Security Tokens are now displayed in the Live Logs section of the IDE"
                    if (ShowTokens) paragraph "Access token:\n${msg}\n\nApplication ID:\n${app.id}"
                    if (ShowTokens) log.trace "STappID = '${app.id}' , STtoken = '${state.accessToken}'"
     	 	}
		        section ("Apache License"){
        			input "ShowLicense", "bool", title: "Show License", default: false, submitOnChange: true
            		def msg1 = textLicense()
            		if (ShowLicense) paragraph "${msg1}"
     		}
    			section("SmartApp Directions"){ 
                	paragraph ("For detailed installation and how-to instruction, follow the link below") 
        	}
    	}		
    }    	
}
  
def about(){
	dynamicPage(name: "about", uninstall: true) {
        section {
        	paragraph "${textAppName()}\n${textVersion()}\n${textCopyright()}",image: "https://raw.githubusercontent.com/BamaRayne/alexaspeaks.src/master/app-Echosistant.png"	//    if (!state.accessToken) OAuthToken()
            def msg = state.accessToken != null ? state.accessToken : "Could not create Access Token. OAuth may not be enabled. Go to the SmartApp IDE settings to enable OAuth."
            paragraph "Access token:\n${msg}\n\nApplication ID:\n${app.id}"
 		}
    	section ("Access Token / Application ID"){
        	href "pageTokens", title: "Show Access Token and Application ID.  Renew/Reset Token", description: none
      	}
        section ("Apache License"){
        	input "ShowLicense", "bool", title: "Show License", default: false, submitOnChange: true
            def msg = textLicense()
            if (ShowLicense) paragraph "${msg}"
     	}
    	section("Instructions") { paragraph ("For detailed installation and how-to instruction, follow the link below") 
        }
        section("Tap below to remove the ${textAppName()} application"){}
	}
}
    def OutputTxt;
            if (outputTxt) {
                url += 'g?data=' + data;
                process = true;
            } 
private def textLicense() {
	def text =
		"Licensed under the Apache License, Version 2.0 (the 'License'); "+
		"you may not use this file except in compliance with the License. "+
		"You may obtain a copy of the License at"+
		"\n\n"+
		"    http://www.apache.org/licenses/LICENSE-2.0"+
		"\n\n"+
		"Unless required by applicable law or agreed to in writing, software "+
		"distributed under the License is distributed on an 'AS IS' BASIS, "+
		"WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. "+
		"See the License for the specific language governing permissions and "+
		"limitations under the License."
}  
 def processTts() {
        def tts = params.ttstext 
        def txt = params.ttstext
		def intent  = params.Param
        tts = PreMsg + tts
        log.trace "data"
return ["outputTxt":outputTxt]
	    
 } 
