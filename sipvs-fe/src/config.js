var ditec = ditec || {};

(function() {
    ditec.config= ditec.config || {};
    var URL_BASE = "https://www.slovensko.sk/static/zep/java_apps/";
    var URL_BASE2 = "https://www.slovensko.sk/static/kep/apps/java/";

    ditec.config.dSigXadesJs = {
        "applet.jnlpUrl" : URL_BASE + "dsigner_applet_2.0.jnlp",
        "dlauncher.jnlpUrl" : URL_BASE + "dsigner_dlauncher_2.0.jnlp",
        "dlauncher2.urlList" : ["ditec-dlauncher2:sk.ditec.ep.products.dsigner.dotnet#action=dbridge", URL_BASE2 + "dsigner_2.x/sk.ditec.ep.products.dsigner.java.xml#action=dbridge"],
        "platforms": null
    };

    ditec.config.dSigXadesBpJs = {
        "applet.jnlpUrl" : URL_BASE + "dsigner_bp_applet_2.0.jnlp",
        "dlauncher.jnlpUrl" : URL_BASE + "dsigner_bp_dlauncher_2.0.jnlp",
        "dlauncher2.urlList" : ["ditec-dlauncher2:sk.ditec.ep.products.dsigner.dotnet#action=dbridge", URL_BASE2 + "dsigner_2.x/sk.ditec.ep.products.dsigner.java.xml#action=dbridge"],
        "platforms": null
    };


    ditec.config.dViewerJs = {
        "applet.jnlpUrl" : URL_BASE + "dviewer_applet_2.0.jnlp",
        "dlauncher.jnlpUrl" : URL_BASE + "dviewer_dlauncher_2.0.jnlp",
        "dlauncher2.urlList" : ["ditec-dlauncher2:sk.ditec.ep.products.dviewer.dotnet#action=dbridge", URL_BASE2 + "dviewer_2.x/sk.ditec.ep.products.dviewer.java.xml#action=dbridge"],
        "platforms": null
    };

    ditec.config.dSigXadesExtenderJs = {
        "applet.jnlpUrl" : URL_BASE + "extender_applet_2.0.jnlp",
        "dlauncher.jnlpUrl" : URL_BASE + "extender_dlauncher_2.0.jnlp",
        "dlauncher2.urlList" : ["ditec-dlauncher2:sk.ditec.ep.products.dsignertools.dotnet#action=dbridge", URL_BASE2 + "extender_2.x/sk.ditec.ep.products.dsignertools.java.xml#action=dbridge"],
        "platforms": null
    };

    ditec.config.dGinaJs = {
        "applet.jnlpUrl" : URL_BASE + "dgina_applet_2.0.jnlp",
        "dlauncher.jnlpUrl" : URL_BASE + "dgina_dlauncher_2.0.jnlp",
        "dlauncher2.urlList" : ["ditec-dlauncher2:sk.ditec.ep.products.dgina.dotnet#action=dbridge", URL_BASE2 + "dgina_2.x/sk.ditec.ep.products.dgina.java.xml#action=dbridge"],
        "platforms": null
    };

    ditec.config.downloadPage = {
        url : "https://www.slovensko.sk/sk/na-stiahnutie",
        title : "slovensko.sk"
    };
}());