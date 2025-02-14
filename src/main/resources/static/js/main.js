document.getElementById("scrollToBottom").addEventListener("click", function() {
    window.scrollTo({ top: document.body.scrollHeight, behavior: "smooth" });
});