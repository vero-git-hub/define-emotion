document.addEventListener("DOMContentLoaded", function() {
    const countrySelect = document.getElementById("countrySelect");
    const citySelect = document.getElementById("citySelect");
    
    function clearCityOptions() {
        while (citySelect.options.length > 0) {
            citySelect.remove(0);
        }
    }

    countrySelect.addEventListener("change", function() {
        const selectedCountry = this.value;
        clearCityOptions();

        if (!selectedCountry) {
            return;
        }

        fetch('/api/cities?country=' + encodeURIComponent(selectedCountry))
            .then(response => response.json())
            .then(data => {
                data.forEach(cityName => {
                    const option = document.createElement("option");
                    option.value = cityName;
                    option.textContent = cityName;
                    citySelect.appendChild(option);
                });
            })
            .catch(error => console.error("Error fetching cities:", error));
    });
});