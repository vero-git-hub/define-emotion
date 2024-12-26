document.addEventListener("DOMContentLoaded", function() {
    const helpBtn = document.getElementById("helpBtn");
    const helplineResults = document.getElementById("helplineResults");
    const helplineList = document.getElementById("helplineList");
    
    if (helpBtn) {
        helpBtn.addEventListener("click", function() {
            helplineResults.style.display = "block";
            
            const country = document.getElementById("countryInput")?.textContent?.trim();
            const city = document.getElementById("cityInput")?.textContent?.trim();

            if (!country || !city) {
                alert("Country or city is not specified.");
                return;
            }

            fetch('/api/helplines?country=' + encodeURIComponent(country) + '&city=' + encodeURIComponent(city))
                .then(response => {
                    if (!response.ok) {
                        throw new Error("Network response was not ok");
                    }
                    return response.json();
                })
                .then(data => {
                    if (data.length === 0) {
                        helplineList.innerHTML = "<li>No helplines found.</li>";
                    } else {
                        data.forEach(item => {
                            const tr = document.createElement("tr");
                            tr.innerHTML = `
                                <td>${item.name}</td>
                                <td>
                                    ${item.phone ? `<a class="btn btn-sm btn-success" href="tel:${item.phone}">
                                        <i class="fas fa-phone-alt"></i></a>` : ""}
                                </td>
                                <td>
                                    ${item.website ? `<a class="btn btn-sm btn-primary ms-2" href="${item.website}" target="_blank">
                                        <i class="fas fa-globe"></i></a>` : ""}
                                </td>
                                <td>
                                    ${item.text ? `<a class="btn btn-sm btn-primary ms-2" href="${item.text}" target="_blank">
                                        <i class="fas fa-sms"></i></a>` : ""}
                                </td>
                            `;
                            helplineList.appendChild(tr);
                        });
                    }
                })
                .catch(error => {
                    console.error("Error fetching helplines:", error);
                    helplineList.innerHTML = "<li>Error fetching helplines. Please try again later.</li>";
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