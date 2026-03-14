package pro.turkninja.saas.provider;

public record Storefront(
        String bio,              // Hakkımda yazısı
        String themeColor,       // Tema rengi (Örn: #ff0000)
        String bannerImageUrl,   // Arka plan kapak fotoğrafı URL'si
        String profileImageUrl   // Profil fotoğrafı veya Logo
) {}
