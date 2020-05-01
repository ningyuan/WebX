/**
 * 
 */

function loadJSON(path, process)
{
    var xhr = new XMLHttpRequest();
    
    xhr.onreadystatechange = function()
    {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                process(JSON.parse(xhr.responseText)); 
            } 
            else {
                process(null);
            }
        }
    };
    
  
    xhr.open("GET", path, false);
    xhr.send();
}