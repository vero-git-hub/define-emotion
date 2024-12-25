document.addEventListener("DOMContentLoaded", function() {
    const helpBtn = document.getElementById("helpBtn");
    const helplineResults = document.getElementById("helplineResults");
    const helplineList = document.getElementById("helplineList");

    if (helpBtn) {
        helpBtn.addEventListener("click", function() {
            helplineList.innerHTML = "";
            helplineResults.style.display = "block";

            const country = /*[[${country}]]*/ '';
            const city = /*[[${city}]]*/ '';

            if (!country || !city) {
                alert("Country or city is not specified.");
                return;
            }

            fetch(`/api/helplines?country=${encodeURIComponent(country)}&city=${encodeURIComponent(city)}`)
                .then(response => response.json())
                .then(data => {
                    if (data.length === 0) {
                        helplineList.innerHTML = "<li>No helplines found.</li>";
                    } else {
                        data.forEach(item => {
                            const li = document.createElement("li");
                            li.innerHTML = `
                                <strong>${item.name}</strong> 
                                (Phone: ${item.phone}) 
                                <button class="btn btn-sm btn-success ms-2" onclick="callHotline('${item.phone}')">Call</button>
                            `;
                            helplineList.appendChild(li);
                        });
                    }
                })
                .catch(error => {
                    console.error("Error fetching helplines:", error);
                    helplineList.innerHTML = "<li>Error fetching helplines.</li>";
                });
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