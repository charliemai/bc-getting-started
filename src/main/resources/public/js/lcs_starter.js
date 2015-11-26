//
// Copyright 2015 LINE Corporation
//
// LINE Corporation licenses this file to you under the Apache License,
// version 2.0 (the "License"); you may not use this file except in compliance
// with the License. You may obtain a copy of the License at:
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
// WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
// License for the specific language governing permissions and limitations
// under the License.

// This is a sample code that show you how to program with JS SDK.
// You can easily custimze it.

// ex: exports.history equals to window.history
(function(exports) {
	// debug msg will be appended in body directly.
	function debug(msg) {
		var p = document.createElement('p');
		p.innerHTML = msg;
		document.body.appendChild(p);
	}

	function main() {
		var eLCS = exports.LCS;
		if (!eLCS) {
			document.write('Cannot find window.LCS variable, perhaps you haven\'t put ' +
				'"<script src="https://scdn.line-apps.com/channel/sdk/js/loader_20150909.js"></script>"' +
				' in the <head></head>');
		}

		// LCS.Interface.registerTitleBarCallback
		// https://developers.line.me/in_app_web/titlebar-control#handling_event
		eLCS.Interface.registerTitleBarCallback(function(evt) {
			switch(evt.target) {
				// Warnning: Left button will not show in the subpage of iOS UIWebView
			  case "LBUTTON":
			  	leftButtonClick();
			  	break;
			  case "BACK":
			  	backButtonClick();
			    break;
			  case "RBUTTON":
			  	rightButtonClick();
			  	break;
			  case "CLOSE":
			  	closeButtonClick();
			    break;
			  case "TITLE":
					titleClick();
					break;
			 }
		});

		// LCS.Interface.updateTitleBar
		// https://developers.line.me/in_app_web/titlebar-control#updating_native_bar
		// TIPS: You can put it call LCS.Interface.updateTitleBar in each of your
		// 			 page, but remeber, left button doesn't exist in the subpage of
		//			 iOS's UIWebView
		eLCS.Interface.updateTitleBar({
		  pageKey: "starter_page",
		  titleBar: {
		  	// Left button would not exist in subpage of iOS UIWebView.
		    left: {
		      imgId: "btn_default",
		      text: "Back",
		      visible: true,
		      enable: true
		    },
		    center: {
		      text: "Starter Page",
		      clickable: true
		    },
		    right: {
		      imgId: "btn_default",
		      text: "Close",
		      visible: true,
		      enable: true
		    }
		  }
		});

		// LCS.Interface.getProfile
		// https://developers.line.me/in_app_web/api-reference#getting_profile
		// tips: You can easily custimze the success and error callback
		eLCS.Interface.getProfile(onGetProfileSuccess, onGetProfileError);
	}

	// info.pictureUrl
	// info.statusMessage
	// info.id , user's mid
	// info.displayName
	function onGetProfileSuccess(info) {
		var container = document.createElement('div');
		container.innerHTML = '<img src=' + info.pictureUrl + '>' +
			'<p>name: ' + info.displayName + '</p>' +
			'<p>mid: ' + info.id + '</p>' +
			'<p>status: ' + info.statusMessage + '</p>';
		document.body.appendChild(container);
	}

	function onGetProfileError() {
		document.write('cannot get user profile');
	}

	// tips: you can use window.onRightTitleButtonClicked function or
	//			 custimze it by yourself. The default function is to
	//			 close the in-app web
	function rightButtonClick() {
		if (exports.onRightTitleButtonClick) {
			exports.onRightTitleButtonClicked();
		} else {
			closeWindow();
		}
	}

	// tips: you can use window.onLeftButtonClick function or
	//			 custimze it by yourself. The default function is ths same as
	//			 back button's function.
	function leftButtonClick() {
		if (exports.onLeftButtonClick) {
			exports.onLeftButtonClick();
		} else {
			backButtonClick();
		}
	}

	// tips: you can use window.onBackButtonClick function or
	//			 custimze it by yourself. The default function is going back to
	//			 previous page, if not previous page, it will close the in-app web.
	function backButtonClick() {
		if (exports.onBackButtonClick) {
			exports.onBackButtonClick();
		} else {
			if (exports.history.length > 1) {
				exports.history.back();
			} else {
				closeWindow();
			}
		}
	}

	// tips: you can use window.onCloseButtonClick function or
	//			 custimze it by yourself. The default function is closing the in-app web.
	function closeButtonClick() {
		if (exports.onCloseButtonClick) {
			exports.onCloseButtonClick();
		} else {
			closeWindow();
		}
	}

	function titleClick() {
		// alert('title is clicked.');
	}

	function closeWindow() {
		// LCS.Interface.closeWebView()
		exports.LCS.Interface.closeWebView();
	}

	// Make sure the main function is launched after deviceready event.
	document.addEventListener('deviceready', main);

}(window))
