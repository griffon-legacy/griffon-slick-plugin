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

/**
 * @author Andres Almiray
 */
class SlickGriffonPlugin {
    // the plugin version
    String version = "0.3"
    // the version or versions of Griffon the plugin is designed for
    String griffonVersion = '1.0.0 > *' 
    // the other plugins this plugin depends on
    Map dependsOn = [swing: '1.0.0', lwjgl: '0.5']
    // resources that are included in plugin packaging
    List pluginIncludes = []
    // the plugin license
    String license = 'BSD'
    // Toolkit compatibility. No value means compatible with all
    // Valid values are: swing, javafx, swt, pivot, gtk
    List toolkits = ['swing']
    // Platform compatibility. No value means compatible with all
    // Valid values are:
    // linux, linux64, windows, windows64, macosx, macosx64, solaris
    List platforms = []
    String documentation = ''
    String source = 'https://github.com/griffon/griffon-slick-plugin'

    List authors = [
        [
            name: 'Andres Almiray',
            email: 'aalmiray@yahoo.com'
        ]
    ]
    String title = 'Slick2D is a simple set of tools wrapped around the LWJGL OpenGL binding for Java'
    def description = '''
Provides integration with [Slick2D][1], a simple set of tools wrapped around the LWJGL OpenGL binding for Java, used mainly for games.

Usage
-----

This plugin enables two modes of operation: basic and state-based.

### Basic Mode

In the first mode a single slick View provides the entry point for the application. It relies on a custom implementation of 
`org.newdawn.slick.BasigGame` to launch and execute the game. The behavior of every method can be overridden by setting a closure that
matches a naming convention: `on<MethodName>`. These are all the possible closures that can be set on the game delegate: 

onInit, onRender, onUpdate, acceptingInput, onCloseRequested, onControllerButtonPressed, onControllerButtonReleased, onControllerDownPressed,
onControllerDownReleased, onControllerLeftPressed, onControllerLeftReleased, onControllerRightPressed, onControllerRightReleased,
onControllerUpPressed, onControllerUpReleased, onInputEnded, onInputStarted, onKeyPressed, onKeyReleased, onMouseClicked, onMouseDragged,
onMouseMoved, onMousePressed, onMouseReleased, onMouseWheelMoved, onSetInput.

**Note** `acceptingInput` is the only closure whose name does not follow the convention.

### Events

The following events will be triggered by this basic mode

 * **SlickInit[app, gc]** - triggered when the game inits itself
 * **SlickUpdate[app, gc, delta]** - triggered when the game has been updated
 * **SlickRender[app, gc, g]** - triggered when the game has completed a render pass


### State Based Mode

In state-based mode the game is built around a set of states. Each state is defined in its own MVC group. A custom implementation of
`org.newdawn.slick.state.StateBasedGame` is used to connect each state. The behavior of every method can be overridden by setting a
closure that matches a naming convention: `on<MethodName>`. These are all the possible closures that can be set on the game delegate:

onInit, onRender, onUpdate, acceptingInput, onCloseRequested, onControllerButtonPressed, onControllerButtonReleased, onControllerDownPressed,
onControllerDownReleased, onControllerLeftPressed, onControllerLeftReleased, onControllerRightPressed, onControllerRightReleased,
onControllerUpPressed, onControllerUpReleased, onInputEnded, onInputStarted, onKeyPressed, onKeyReleased, onMouseClicked, onMouseDragged,
onMouseMoved, onMousePressed, onMouseReleased, onMouseWheelMoved, onSetInput, onEnter, onLeave.

**Note** `acceptingInput` is the only closure whose name does not follow the convention.

### Events

The following events will be triggered by this basic mode

 * **SlickInit[app, gc, game]** - triggered when the game inits itself
 * **SlickUpdate[app, gc, game, delta]** - triggered when the game has been updated
 * **SlickRender[app, gc, game, g]** - triggered when the game has completed a render pass
 * **SlickInitStates[game]** - triggered when states should be added to the game
 * **SlickPreRenderState[container, g]** - triggered before rendering a state
 * **SlickPostRenderState[container, g]** - triggered after a state has been rendered
 * **SlickPreUpdateState[container, delta]** - triggered before updating a state
 * **SlickPostUpdateState[container, delta]** - triggered after a state has been updated

Scripts
-------

 * **create-game-state** - creates a new GameState class in griffon-app/slick-states
 * **convert-view** - transforms a View script into a View that can be used with BasicSlickGriffonApplication

### Example

Here's how the [Basic Slick sample][2] from the [Tutorials][3] can be implemented with Griffon.

1. Create a new application

        griffon create-app sample
        cd sample

2. Install the slick plugin

        griffon install-plugin slick

3. Convert the generated View into a Slick View.

        griffon convert-view sample.SampleView

It should now look like this

__griffon-app/views/sample/SampleView.groovy__

        package sample
 
        application(title: 'sample',
          size: [800, 600],
          icon: 'griffon-icon-48x48.png',
          icons: ['griffon-icon-48x48.png',
                  'griffon-icon-32x32.png',
                  'griffon-icon-16x16.png']) {
            app.game.onRender = { container, g ->
                // render game contents
            }
        }

4. Update the view code so that it renders a plane and the map. Where do the plane and map references come from? Don't worry, we'll setup
those references in the next step.

__griffon-app/views/sample/SampleView.groovy__

        package sample
 
        application(title: 'sample',
          size: [800, 600],
          icon: 'griffon-icon-48x48.png',
          icons: ['griffon-icon-48x48.png',
                  'griffon-icon-32x32.png',
                  'griffon-icon-16x16.png']) {
            app.game.onRender = { container, g ->
                model.with {
                    land.draw(0, 0)
                    plane.draw(x, y, scale)
                }
            }
        }

5. Add a plane and map properties to the Model.

__griffon-app/models/sample/SampleModel.groovy__

        package sample
 
        import org.newdawn.slick.Image
 
        class SampleModel {
            Image plane
            Image land
            float x = 400f
            float y = 300f
            float scale = 1f
 
            void load() {
                land = new Image('data/land.jpg')
                plane = new Image('data/plane.png')
            }
        }

Pay close attention to the `load()` method. It must be called once the game has been initialized, which will do in the Controller.

6. Replace the Controller with a Java artifact. By default the Controller will be Groovy based. Why do we need to replace the Controller?
Just to demonstrate how legacy code can be reused.

        griffon replace-artifact --file-type=java --type=controller sample.SampleController

7. Edit the Controller by filling the missing game logic

__griffon-app/controllers/sample/SampleController.java__

        package sample;
 
        import griffon.core.GriffonApplication;
        import org.newdawn.slick.Input;
        import org.newdawn.slick.Image;
        import org.newdawn.slick.GameContainer;
        import org.codehaus.griffon.runtime.core.AbstractGriffonController;
 
        public class SampleController extends AbstractGriffonController {
            private SampleModel model;
 
            public void setModel(SampleModel model) { this.model = model; }
 
            public void onSlickInit(GriffonApplication app, GameContainer container) {
                model.load();
            }
 
            public void onSlickUpdate(GriffonApplication app, GameContainer container, int delta) {
                Input input = container.getInput();
                Image plane = model.getPlane();
 
                if(input.isKeyDown(Input.KEY_A)) {
                    plane.rotate(-0.2f * delta);
                }
 
                if(input.isKeyDown(Input.KEY_D)) {
                    plane.rotate(0.2f * delta);
                }
 
                if(input.isKeyDown(Input.KEY_W)) {
                    float hip = 0.4f * delta;
 
                    float rotation = plane.getRotation();
 
                    double x = model.getX() + (hip * Math.sin(Math.toRadians(rotation)));
                    double y = model.getY() - (hip * Math.cos(Math.toRadians(rotation)));
                    model.setX((float) x);
                    model.setY((float) y);
                }
 
                float scale = model.getScale();
                if(input.isKeyDown(Input.KEY_Z)) {
                    scale += (scale >= 5.0f) ? 0 : 0.1f;
                    plane.setCenterOfRotation(plane.getWidth()/2.0f*scale, plane.getHeight()/2.0f*scale);
                }
                if(input.isKeyDown(Input.KEY_O)) {
                    scale -= (scale <= 1.0f) ? 0 : 0.1f;
                    plane.setCenterOfRotation(plane.getWidth()/2.0f*scale, plane.getHeight()/2.0f*scale);
                }
                model.setScale(scale);
            }
        }

Notice we take advantage of the onSlickInit event to initialize the Model at the right time.

8. Locate and download the `plane.png` and `land.jpg` assets from Basic Slick sample. Place them under `griffon-app/resources/data`.

9. Run the application

        griffon run-app

The following keys are enabled

 * **A** - rotates the plane counter clock-wise.
 * **D** - rotates the plane clock-wise.
 * **W** - moves the plane forward.
 * **Z** - increases the zoom level of the plane sprite.
 * **O** - increases the zoom level of the plane sprite.

Be careful when moving the plane forward; this game is so simple it does not perform collision detection, chances are that when the
plane leaves the viewport you won't see it again unless you manage to rotate it to the correct angle.


[1]: http://slick.cokeandcode.com/
[2]: http://slick.cokeandcode.com/wiki/doku.php?id=01_-_a_basic_slick_game
[3]: http://slick.cokeandcode.com/wiki/doku.php?id=tutorials
'''
}
