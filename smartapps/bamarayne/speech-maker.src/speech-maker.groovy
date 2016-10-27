/**
 *  Speech Maker
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
    name: "Speech Maker",
    namespace: "bamarayne",
    author: "Jason Headley",
    description: "Just a simple app to generate TTS",
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	// TODO: put inputs here
        section("Switch On Message") {
        	input "audioTextOn", "text", title: "Play this message when a switch is turned on", description: "Enter a message to play", required: false, capitalization: "sentences"
            }
        section("Switch Off Message") {    
           input "audioTextOff", "text", title: "Play this message when a swtich is turned off", description: "Enter a message to play", required: false, capitalization: "sentences"
            }
        section("Speech Devices for Playback") {   
            input "audioDevice", "capability.musicPlayer", title: "Choose Speech Device", required: false 
	}
 }  
        section("Choose the switch:") {
        	input "theSwitch", "capability.switch", required: false, title: "Switch?"
    }


def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
	// TODO: subscribe to attributes, devices, locations, etc.
	subscribe(theSwitch, "switch.on", switchOnHandler)
    subscribe(theSwitch, "switch.off", switchOffHandler)
}

// TODO: implement event handlers
def switchOnHandler(evt) {
	log.debug "switchOnHandler called: $evt"
  	audioDevice.speak(playTextOn)
}
def switchOffHandler(evtOff) {
	log.debug "switchOffHandler called: $evtOff"
    audioDevice.speak(playTextOff)
}
    
    //if("on" == evt.value && "on" != audioDevice.currentDevice) {
    	
  //	} else if ("off" == evt.value && "off" != audioDevice.currentDevice) {
  // 		myswitch.off()
 // }
