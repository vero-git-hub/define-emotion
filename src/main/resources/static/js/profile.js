document.addEventListener("DOMContentLoaded", function () {
    const countrySelect = document.getElementById("countrySelect");
    const citySelect = document.getElementById("citySelect");

    if (!countrySelect || !citySelect) {
        return;
    }

    function clearCityOptions() {
        while (citySelect.options.length > 0) {
            citySelect.remove(0);
        }
    }

    function populateCities(country, selectedCity) {
        clearCityOptions();

        if (!country) {
            return;
        }

        fetch('/api/cities?country=' + encodeURIComponent(country))
            .then(response => response.json())
            .then(data => {
                data.forEach(cityName => {
                    const option = document.createElement("option");
                    option.value = cityName;
                    option.textContent = cityName;
                    if (cityName === selectedCity) {
                        option.selected = true;
                    }
                    citySelect.appendChild(option);
                });
            })
            .catch(error => console.error("Error fetching cities:", error));
    }

    const currentCountry = countrySelect.value;
    const currentCity = citySelect.getAttribute("data-selected-city");
    if (currentCountry) {
        populateCities(currentCountry, currentCity);
    }

    countrySelect.addEventListener("change", function () {
        const selectedCountry = this.value;
        populateCities(selectedCountry, null);
    });
});