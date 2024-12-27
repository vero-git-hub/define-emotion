document.addEventListener("DOMContentLoaded", function () {
    const analyzeBtn = document.getElementById("analyzeBtn");
    const textInput = document.getElementById("textInput");
    const analysisResults = document.getElementById("analysisResults");

    if (analyzeBtn) {
        analyzeBtn.addEventListener("click", function () {
            const text = textInput.value.trim();

            if (!text) {
                analysisResults.style.display = "block";
                analysisResults.classList.remove("alert-info");
                analysisResults.classList.add("alert-danger");
                analysisResults.textContent = "Please enter some text for analysis.";
                return;
            }

            fetch("/api/analyze", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
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
                    analysisResults.style.display = "block";
                    analysisResults.classList.remove("alert-danger");
                    analysisResults.classList.add("alert-info");
                    analysisResults.textContent = `Analysis Result: ${data.result}`;
                })
                .catch(error => {
                    console.error("Error analyzing text:", error);
                    analysisResults.style.display = "block";
                    analysisResults.classList.remove("alert-info");
                    analysisResults.classList.add("alert-danger");
                    analysisResults.textContent = "Error analyzing the text. Please try again later.";
                });
        });
    }
});