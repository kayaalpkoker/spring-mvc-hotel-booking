// Initialize the map for the given hotel address
function initializeHotelMap(hotelAddress) {
    // Initial position is a default location
    let lat = 40.99046;
    let lon = 29.02916;

    let map = L.map('map').setView([lat, lon], 13);

    // Add OpenStreetMap tile layer to the map
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(map);

    // Geocode address and reposition the map
    fetch(`https://nominatim.openstreetmap.org/search?format=json&limit=1&q=${encodeURIComponent(hotelAddress)}`)
        .then(response => response.json())
        .then(data => {
            if (data[0]) {
                lat = parseFloat(data[0].lat);
                lon = parseFloat(data[0].lon);
                map.setView([lat, lon], 13);
                L.marker([lat, lon]).addTo(map);
            }
        });
}