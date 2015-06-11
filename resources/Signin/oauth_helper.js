var helper = (function() {
  var authResult = undefined;

  return {
    onSignInCallback: function(authResult) {
		//alert("ONSIGNINCALLBACK");
      if (authResult['access_token']) {
        // The user is signed in
		//alert("ALREADY SIGNED IN");
        this.authResult = authResult;
      } else if (authResult['error']) {
		//alert("AUTHRESULT ERROR");
	
      }
    },

    disconnectServer: function() {
      // Revoke the server tokens
      $.ajax({
        type: 'POST',
        url: $(location).attr('origin') + '/cocoon/vicar/disconnect',
        async: false,
        success: function(result) {
		//alert("DISC "+result);
        },
        error: function(e) {
        }
      });
    },

    connectServer: function(code) {
	var state_val = $('#state').val();
      $.ajax({
        type: 'POST',
	url: $(location).attr('origin') + '/cocoon/vicar/connect?state='+state_val,
        contentType: 'application/octet-stream; charset=utf-8',
        success: function(result) {
		//alert("CONN "+result);
		helper.vicar_redir();
		onSignInCallback(auth2.currentUser.get().getAuthResponse());
        },
	error: function(xhr,ajaxObjection,thrownError){
		alert("CONNECT ERROR STATUS "+xhr.status+" TEXT "+xhr.responseText+" THROWN "+thrownError);
	},
        processData: false,
        data: code
      });
    },

    vicar_redir: function(success, failure) {
      $.ajax({
        type: 'GET',
        url: $(location).attr('origin') + '/cocoon/vicar/vicar_redir',
        contentType: 'application/octet-stream; charset=utf-8',
        success: function(result){
		//alert("RESULT "+result);
		location.replace(result);
		//location.replace("Vicar.html");
	},
        error: failure,
	//error: function(xhr,ajaxObjection,thrownError){
	//	alert("REDIR ERROR STATUS "+xhr.status+" TEXT "+xhr.responseText+" THROWN "+thrownError);
	//},
        processData: false
      });
    },
  };
})();

$(document).ready(function() {
  $('#disconnect').click(helper.disconnectServer);
  if ($('[data-clientid="YOUR_CLIENT_ID"]').length > 0) {
//	alert('This sample requires your OAuth credentials (client ID) ' +
//	'from the Google APIs console:\n' +
//        '    https://code.google.com/apis/console/#:access\n\n' +
//        'Find and replace YOUR_CLIENT_ID with your client ID and ' +
//        'YOUR_CLIENT_SECRET with your client secret in the project sources.'
//    );
  }
});

function startApp() {
  gapi.load('auth2', function(){
	var clientid_val = $('#clientid').val();
//	alert("CID "+clientid_val);
    // Retrieve the singleton for the GoogleAuth library and setup the client.
    gapi.auth2.init({
        client_id: clientid_val,
        cookiepolicy: 'single_host_origin',
        fetch_basic_profile: false,
        scope: 'https://www.googleapis.com/auth/plus.login'
      }).then(function (){
            console.log('init');
            auth2 = gapi.auth2.getAuthInstance();
            auth2.then(function() {
                var isAuthedCallback = function () {
                  onSignInCallback(auth2.currentUser.get().getAuthResponse())
                }
                helper.vicar_redir(isAuthedCallback);
              });
          });
  });
}

function signInClick() {
//	alert("signInClick");
  var signIn = function(result) {
      auth2.signIn().then(
        function(googleUser) {
          onSignInCallback(googleUser.getAuthResponse());
        }, function(error) {
          alert(JSON.stringify(error, undefined, 2));
        });
    };

  var reauthorize = function() {
//	alert("REAUTHORIZE");
      auth2.grantOfflineAccess().then(
        function(result){
          helper.connectServer(result.code);
        });
    };

  helper.vicar_redir(signIn, reauthorize);
}

function onSignInCallback(authResult) {
  helper.onSignInCallback(authResult);
}

