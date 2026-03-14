/** @type {import('tailwindcss').Config} */
module.exports = {
    content: [
        "./src/main/resources/templates/**/*.html", // Thymeleaf dosyalarımız burada
        "./src/main/resources/static/**/*.js"
    ],
    theme: {
        extend: {},
    },
    plugins: [
        require('daisyui'), // Hazır UI bileşenleri için DaisyUI eklendi
    ],
    daisyui: {
        themes: ["light", "dark", "corporate"], // SaaS için şık temalar
    },
}
