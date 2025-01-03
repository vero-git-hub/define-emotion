document.addEventListener("DOMContentLoaded", function () {
    const analyzeBtn = document.getElementById("analyzeBtn");
    const textInput = document.getElementById("textInput");
    const analysisResults = document.getElementById("analysisResults");
    const saveSection = document.getElementById("saveSection");
    const mainButtons = document.getElementById("mainButtons");
    const cancelBtn = document.getElementById("cancelBtn");
    const csrfToken = document.querySelector('input[name="_csrf"]').value;
    const saveBtn = document.getElementById("saveBtn");
    const form = document.querySelector("form");

    if (saveBtn) {
        saveBtn.addEventListener("click", function () {
            textInput.disabled = false;
            form.submit();
        });
    }

    if (analyzeBtn) {
        analyzeBtn.addEventListener("click", function () {
            const text = textInput.value.trim();

            if (!isValidText(text)) {
                showError("Invalid input. Please enter meaningful text with at least 3 characters.");
                return;
            }

            fetch("/emotions/analyze", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "X-CSRF-Token": csrfToken,
                },
                body: JSON.stringify({ text }),
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error("Failed to analyze the text");
                    }
                    return response.json();
                })
                .then(data => {
                    if (data.error) {
                        showError(data.error);
                        return;
                    }

                    analysisResults.style.display = "block";
                    analysisResults.classList.remove("alert-danger");
                    analysisResults.classList.add("alert-info");
                    analysisResults.innerHTML = `
                        <strong>Analysis Result:</strong> ${data.result}<br>
                        <strong>Advice:</strong> ${data.advice}
                    `;

                    const emotionResult = document.getElementById("emotionResult");
                    emotionResult.value = data.result;

                    textInput.disabled = true;
                    saveSection.style.display = "block";
                    mainButtons.style.display = "none";
                })
                .catch(error => {
                    console.error("Error analyzing text:", error);
                    showError("Error analyzing the text. Please try again later.");
                });
        });
    }

    if (cancelBtn) {
        cancelBtn.addEventListener("click", function () {
            textInput.value = "";
            textInput.disabled = false;
            analysisResults.style.display = "none";
            saveSection.style.display = "none";
            mainButtons.style.display = "block";
        });
    }
});

function isValidText(text) {
    const sanitizedText = text.replaceAll(/[^a-zA-Z0-9\s]/g, "").trim();
    return sanitizedText.length >= 3;
}

function showError(message) {
    const analysisResults = document.getElementById("analysisResults");
    analysisResults.style.display = "block";
    analysisResults.classList.remove("alert-info");
    analysisResults.classList.add("alert-danger");
    analysisResults.textContent = message;
}