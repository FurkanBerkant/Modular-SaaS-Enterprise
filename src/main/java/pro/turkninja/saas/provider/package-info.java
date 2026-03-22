@org.springframework.modulith.ApplicationModule(
        displayName = "Sağlayıcı (Provider) Modülü",
        allowedDependencies = {"appointment", "appointment::events", "security", "tenant"}
)
package pro.turkninja.saas.provider;