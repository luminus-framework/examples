$("#submit-button").click(function(){
    var data = new FormData();
    data.append("file", document.getElementById("fileselect").files[0]);
    $.ajax({
        url: "/upload",
        headers: {"x-csrf-token": $("#csrf-token").val()},
        data: data,
        cache: false,
        contentType: false,
        processData: false,
        type: "POST",
        xhr: function() {
            xhr = $.ajaxSettings.xhr();
            if(xhr.upload){
                xhr.upload.addEventListener("progress",
                function(e) {
                  var pc = parseInt(e.loaded / e.total * 100);
                  $("#upload-progress")
                  .css("width", pc +"%")
                  .attr("aria-valuenow", 100 - pc)
                  .html(pc + "%");
                },
                false);
            }
            return xhr;
        },
        error: function(data) {
          $("#status-message")
          .removeClass("alert-success")
          .toggleClass("alert-danger")
          .html("upload error");
        },
        success: function(data){
          $("#status-message")
          .removeClass("alert-danger")
          .toggleClass("alert-success")
          .html("upload success");
        }});
    return false;
});
