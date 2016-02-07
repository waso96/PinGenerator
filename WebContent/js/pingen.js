function goHome() {
	window.location.replace(window.url_home);
}

function menuPinGenBatch() {
	Ink.requireModules(['Ink.Net.Ajax_1','Ink.Dom.Element_1'], function(Ajax,InkElement) {
		var container = Ink.i('main-panel');
		Ajax.load('pingen-batch.html', function (res) {
		    InkElement.setHTML(container,res);
		});
	});
}

function menuPinGenSpecific() {
	alert("spec");
}

function pinGenButtonGenerateClick() {
	Ink.requireModules(['Ink.Net.Ajax_1', 'Ink.Dom.FormSerialize_1'], function(Ajax, FormSerialize) {

	    var form = Ink.i('formPinGenBatch');

        var formData = FormSerialize.serialize(form);
        //Ink.log(formData);
        var uri = window.url_home + 'PinGen';
        new Ajax(uri, {
            method: 'POST',
            postBody: formData,
            onSuccess: function(obj) {
                if(obj && obj.responseJSON) {
                	Ink.log("success " + obj.responseJSON['jobId']);

                }
            }, 
            onFailure: function() {
                //Ink.warn('Ajax request failed');
            }
        });
	});
}

