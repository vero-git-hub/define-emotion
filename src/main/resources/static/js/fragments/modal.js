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
                                    ${item.phone ? `
                                        <button class="btn btn-sm btn-success" 
                                                onclick="callHotline('${item.phone}')" 
                                                title="Call ${item.phone}">
                                            <i class="fas fa-phone-alt"></i>
                                        </button>` : ""}
                                </td>
                                <td>
                                    ${item.website ? `
                                        <a class="btn btn-sm btn-primary ms-2" 
                                                href="${item.website}" 
                                                target="_blank" 
                                                title="Visit ${item.website}">
                                            <i class="fas fa-globe"></i>
                                        </a>` : ""}
                                </td>
                                <td>
                                    ${item.text ? `
                                        <button class="btn btn-sm btn-primary" 
                                                onclick="handleText('${item.text}')" 
                                                title="Send SMS to ${item.text}">
                                            <i class="fas fa-sms"></i>
                                        </button>` : ""}
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
        alert(`Please call: ${phone}\n(Note: For desktop, ensure a phone app like Skype is configured for 'tel:' links)`);
    }
}

function handleText(number) {
    const isMobile = /Android|iPhone|iPad|iPod|Windows Phone|BlackBerry/i.test(navigator.userAgent);
    if (isMobile) {
        window.location.href = `sms:${number}`;
    } else {
        alert(`Please use your mobile device to send an SMS to: ${number}`);
    }
}