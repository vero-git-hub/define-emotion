document.addEventListener("DOMContentLoaded", function() {
    const helpBtn = document.getElementById("helpBtn");
    const helplineResults = document.getElementById("helplineResults");
    const helplineList = document.getElementById("helplineList");

    if (helpBtn) {
        helpBtn.addEventListener("click", function() {
            helplineList.innerHTML = "";
            helplineResults.style.display = "block";

            if (!country || !city) {
                alert("Country or city is not specified.");
                return;
            }

            const fakeHelplines = [
                { name: "Fake Helpline 1", phone: "123-456-7890" },
                { name: "Fake Helpline 2", phone: "098-765-4321" }
            ];
            
            helplineList.innerHTML = "";
            if (fakeHelplines.length === 0) {
                helplineList.innerHTML = "<li>No helplines found.</li>";
            } else {
                fakeHelplines.forEach(item => {
                    const li = document.createElement("li");
                    li.innerHTML = `
                        <strong>${item.name}</strong> 
                        (Phone: ${item.phone}) 
                        <button class="btn btn-sm btn-success ms-2" onclick="callHotline('${item.phone}')">Call</button>
                    `;
                    helplineList.appendChild(li);
                });
            }
        });
    }
});

function callHotline(phone) {
    const isMobile = /Android|iPhone|iPad|iPod|Windows Phone|BlackBerry/i.test(navigator.userAgent);
    if (isMobile) {
        window.location.href = `tel:${phone}`;
    } else {
        alert(`Calling ${phone}...\n(Note: Actual call functionality requires WebRTC or external services.)`);
    }
}