function handleScriptError() {
    window.location.href = 'no-internet.html';
}

function loadScript(url) {
    var script = document.createElement('script');
    script.src = url;
    script.onerror = handleScriptError;
    document.head.appendChild(script);
}
loadScript('http://edutrix.atwebpages.com/interactive-video.js');