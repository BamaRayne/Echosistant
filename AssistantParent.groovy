/**
 *  Assistant - Manager
 *		
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
    name		: "Assistant",
    namespace	: "sb",
    author		: "Jason Headley",
    description	: "The parent app.",
    category	: "My Apps",
    iconUrl		: "https://raw.githubusercontent.com/BamaRayne/alexaspeaks.src/master/app-Echosistant.png",
    iconX2Url	: "https://raw.githubusercontent.com/BamaRayne/alexaspeaks.src/master/app-Echosistant@2x.png",
    iconX3Url	: "https://raw.githubusercontent.com/BamaRayne/alexaspeaks.src/master/app-Echosistant@2x.png")

preferences {
	page(name: "main")
    page(name: "pageAbout")
}

def installed() {
log.debug "Installed with settings: ${settings}"
initialize()
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
        			app(name: "Profiles", appName: "assistantProfile", namespace: "sb", description: "Create New Profile...", multiple: true)	
                }
             }	
     	}
 }
 
