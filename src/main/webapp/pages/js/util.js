/**
 * 
 */

function asyncLoadJSON(path, callback)
{
    var xhr = new XMLHttpRequest();
    
    xhr.onreadystatechange = function()
    {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                callback(JSON.parse(xhr.responseText)); 
            } 
            else {
                callback(null);
            }
        }
    };
    
  
    xhr.open("GET", path, true);
    xhr.send();
}