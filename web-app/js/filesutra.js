var filesutraServer = "http://filesutra.com";

window.filesutra = {
  importFiles: function(callback, options) {
    if (options && options.dialogType == 'iframe') {
      var iframe = document.getElementById(options.parentId)
      iframe.src = filesutraServer;
    } else {
      window.open(filesutraServer, "Filesutra", "width=800, height=600, top=100, left=300");
    }
    window.filesutraCallback = callback;
  }
};

window.onmessage = function (e) {
  var data = e.data;
  if (data.type === 'filesutra') {
    window.filesutraCallback(data.data);
  }
};