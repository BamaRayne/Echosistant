/**
 *  Echosistant - PreRelease Alpha Testing
 *
 *  10/26/2016  version 0.1.2a      Name change
 *	10/26/2016	version 0.1.2		Added Icons by @SBDOBRESCU.
 *	10/25/2016	version 0.1.1		Bug Fix in the Token Renew process
 *  10/25/2016  version 0.1.0		Alpha Testing of code complete.
 *	10/23/2016	version 0.0.2		Restrictions operational (modes/days/hours), Sonos operational.		
 *	10/23/2016	version 0.0.1i		Day restriction added, code clean up, UI changes
 *	10/20/2016	version 0.0.1h		Mode restriction working
 *  10/18/2016	version 0.0.1g		Access Token Reset fixed. Multiple UI Changes.  **VERIFY YOUR PROFILES AFTER UPDATE**
 *  10/17/2016	version 0.0.1f		bug fixes. Sonos, media player, speech synthesizer working.  **SONOS STILLS NEEDS TESTING**
 *	10/17/2016 	version 0.0.1e 		bug fixes.
 *  10/15-2016	version 0.0.1d		Added custom "Pre-messages", UI changes
 * 	10/14/2016 	version 0.0.1c		Added Sonos support and OAuth tokens to logs for copy and paste
 *	10/11/2016	version 0.0.1b		Fixed audio output for both media and synth
 *	10/10/2016 	Version 0.0.1a		Added media player support
 *	10/09/2016	Version 0.0.1		Initial File
 *
 /******************* ROADMAP ********************
  - External TTS
 *
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
    name: "Echosistant",
    namespace: "bamarayne",
    author: "Jason Headley",
    description: "A free-form Speech-to-Text SmartApp using the Amazon Echo (Alexa) device.",
    category: "Convenience",
