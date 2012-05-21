/*
 * Copyright (c) 2010-2012 Griffon Slick - Andres Almiray. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *  o Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 *  o Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 *  o Neither the name of Griffon Slick - Andres Almiray nor the names of
 *    its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import griffon.util.GriffonNameUtils
import griffon.util.GriffonUtil

includeTargets << griffonScript("CreateIntegrationTest")

target(createGameState: "Creates a new Game State") {
    if (isPluginProject && !isAddonPlugin) {
        println """You must create an Addon descriptor first.
Type in griffon create-addon then execute this command again."""
        System.exit(1)
    }

    promptForName(type: "Game State")
    def (pkg, name) = extractArtifactName(argsMap['params'][0])

    mvcPackageName = pkg ? pkg : ''
    mvcClassName = GriffonUtil.getClassNameRepresentation(name)
    mvcFullQualifiedClassName = "${pkg ? pkg : ''}${pkg ? '.' : ''}$mvcClassName"

    // -- compatibility
    argsMap['with-model']      = argsMap['with-model']      ?: argsMap.withModel
    argsMap['with-controller'] = argsMap['with-controller'] ?: argsMap.withController
    argsMap['skip-model']      = argsMap['skip-model']      ?: argsMap.skipModel
    argsMap['skip-controller'] = argsMap['skip-controller'] ?: argsMap.skipController
    // -- compatibility

    String modelTemplate      = 'Model'
    String stateTemplate      = 'GameState'
    String controllerTemplate = 'Controller'
    if (argsMap.group) {
        modelTemplate      = argsMap.group + modelTemplate
        stateTemplate      = argsMap.group + stateTemplate
        controllerTemplate = argsMap.group + controllerTemplate
    }

    String modelClassName = ''
    if (!argsMap['skip-model'] && !argsMap['with-model']) {
        createArtifact(
                name:     mvcFullQualifiedClassName,
                suffix:   'Model',
                type:     'Model',
                template: modelTemplate,
                path:     'griffon-app/models')
        modelClassName = fullyQualifiedClassName
    }

    String stateClassName = ''
    createArtifact(
            name:     mvcFullQualifiedClassName,
            suffix:   'GameState',
            type:     'GameState',
            template: stateTemplate,
            path:     'griffon-app/gamestates')
    stateClassName = fullyQualifiedClassName

    String controllerClassName = ''
    if (!argsMap['skip-controller'] && !argsMap['with-controller']) {
        createArtifact(
                name:     mvcFullQualifiedClassName,
                suffix:   'Controller',
                type:     'Controller',
                template: controllerTemplate,
                path:     'griffon-app/controllers')
        controllerClassName = fullyQualifiedClassName

        doCreateIntegrationTest(
                name:   mvcFullQualifiedClassName,
                suffix: '')
    }

    name = GriffonNameUtils.getPropertyName(name)

    if (isAddonPlugin) {
        // create mvcGroup in a plugin
        def isJava = isAddonPlugin.absolutePath.endsWith('.java')
        def addonFile = isAddonPlugin
        def addonText = addonFile.text

        if (isJava) {
            if (!(addonText =~ /\s*public Map<String, Map<String, String>>\s*getMvcGroups\(\)\s*\{/)) {
                addonText = addonText.replaceAll(/\}\s*\z/, """
                public Map<String, Map<String, Object>> getMvcGroups() {
                    Map<String, Map<String, Object>> groups = new LinkedHashMap<String, Map<String, Object>>();
                    return groups;
                }
            }
            """)
            }

            List parts = []
            if (!argsMap['skip-model'])      parts << """            {"model",      "${(argsMap['with-model'] ?: modelClassName)}"}"""
                                             parts << """            {"state",       "$stateClassName"}"""
            if (!argsMap['skip-controller']) parts << """            {"controller", "${(argsMap['with-controller'] ?: controllerClassName)}"}"""

            addonFile.withWriter {
                it.write addonText.replaceAll(/\s*Map<String, Map<String, String>> groups = new LinkedHashMap<String, Map<String, String>>\(\);/, """
                    Map<String, Map<String, Object>> groups = new LinkedHashMap<String, Map<String, Object>>();
                    // MVC Group for "$name"
                    groups.put("$name", groupDef(new String[][]{
            ${parts.join(',\n')}
                    }));""")
            }

        } else {

            if (!(addonText =~ /\s*def\s*mvcGroups\s*=\s*\[/)) {
                addonText = addonText.replaceAll(/\}\s*\z/, """
    def mvcGroups = [
    ]
}
""")
            }
            List parts = []
            if (!argsMap['skip-model'])      parts << "            model     : '${(argsMap['with-model'] ?: modelClassName)}'"
                                             parts << "            state      : '$stateClassName'"
            if (!argsMap['skip-controller']) parts << "            controller: '${(argsMap['with-controller'] ?: controllerClassName)}'"

            addonFile.withWriter {
                it.write addonText.replaceAll(/\s*def\s*mvcGroups\s*=\s*\[/, """
    def mvcGroups = [
        // MVC Group for "$name"
        '$name': [
${parts.join(',\n')}
        ],
    """)
            }
        }
    } else {
        // create mvcGroup in an application
        def applicationConfigFile = new File("${basedir}/griffon-app/conf/Application.groovy")
        def configText = applicationConfigFile.text
        if (!(configText =~ /\s*mvcGroups\s*\{/)) {
            configText += """
mvcGroups {
}
"""
        }

        List parts = []
        if (!argsMap['skip-model'])      parts << "        model      = '${(argsMap['with-model'] ?: modelClassName)}'"
                                         parts << "        state       = '$stateClassName'"
        if (!argsMap['skip-controller']) parts << "        controller = '${(argsMap['with-controller'] ?: controllerClassName)}'"

        applicationConfigFile.withWriter {
            it.write configText.replaceAll(/\s*mvcGroups\s*\{/, """
mvcGroups {
    // MVC Group for "$name"
    '$name' {
${parts.join('\n')}
    }
""")
        }
    }
}

setDefaultTarget(createGameState)
