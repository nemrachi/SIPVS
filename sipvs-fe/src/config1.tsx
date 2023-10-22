// Define a TypeScript module named 'DitecConfigModule'
export module DitecConfigModule {
  export class DitecConfig {
    private static readonly URL_BASE = "https://www.slovensko.sk/static/zep/java_apps/";
    private static readonly URL_BASE2 = "https://www.slovensko.sk/static/kep/apps/java/";

    public dSigXadesJs = {
      "applet.jnlpUrl": DitecConfig.URL_BASE + "dsigner_applet_2.0.jnlp",
      "dlauncher.jnlpUrl": DitecConfig.URL_BASE + "dsigner_dlauncher_2.0.jnlp",
      "dlauncher2.urlList": [
        "ditec-dlauncher2:sk.ditec.ep.products.dsigner.dotnet#action=dbridge",
        DitecConfig.URL_BASE2 + "dsigner_2.x/sk.ditec.ep.products.dsigner.java.xml#action=dbridge"
      ],
      platforms: null,

    };

    public dSigXadesBpJs = {
      "applet.jnlpUrl": DitecConfig.URL_BASE + "dsigner_bp_applet_2.0.jnlp",
      "dlauncher.jnlpUrl": DitecConfig.URL_BASE + "dsigner_bp_dlauncher_2.0.jnlp",
      "dlauncher2.urlList": [
        "ditec-dlauncher2:sk.ditec.ep.products.dsigner.dotnet#action=dbridge",
        DitecConfig.URL_BASE2 + "dsigner_2.x/sk.ditec.ep.products.dsigner.java.xml#action=dbridge"
      ],
      platforms: null,
    };

    public dViewerJs = {
      "applet.jnlpUrl": DitecConfig.URL_BASE + "dviewer_applet_2.0.jnlp",
      "dlauncher.jnlpUrl": DitecConfig.URL_BASE + "dviewer_dlauncher_2.0.jnlp",
      "dlauncher2.urlList": [
        "ditec-dlauncher2:sk.ditec.ep.products.dviewer.dotnet#action=dbridge",
        DitecConfig.URL_BASE2 + "dviewer_2.x/sk.ditec.ep.products.dviewer.java.xml#action=dbridge"
      ],
      platforms: null,
    };

    public dSigXadesExtenderJs = {
      "applet.jnlpUrl": DitecConfig.URL_BASE + "extender_applet_2.0.jnlp",
      "dlauncher.jnlpUrl": DitecConfig.URL_BASE + "extender_dlauncher_2.0.jnlp",
      "dlauncher2.urlList": [
        "ditec-dlauncher2:sk.ditec.ep.products.dsignertools.dotnet#action=dbridge",
        DitecConfig.URL_BASE2 + "extender_2.x/sk.ditec.ep.products.dsignertools.java.xml#action=dbridge"
      ],
      platforms: null,
    };

    public dGinaJs = {
      "applet.jnlpUrl": DitecConfig.URL_BASE + "dgina_applet_2.0.jnlp",
      "dlauncher.jnlpUrl": DitecConfig.URL_BASE + "dgina_dlauncher_2.0.jnlp",
      "dlauncher2.urlList": [
        "ditec-dlauncher2:sk.ditec.ep.products.dgina.dotnet#action=dbridge",
        DitecConfig.URL_BASE2 + "dgina_2.x/sk.ditec.ep.products.dgina.java.xml#action=dbridge"
      ],
      platforms: null,
    };

    public downloadPage = {
      url: "https://www.slovensko.sk/sk/na-stiahnutie",
      title: "slovensko.sk",
    };
  }
}
