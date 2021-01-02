import React from "react";
import {BrowserRouter, Link, Route, Switch} from "react-router-dom";
import Home from "./components/Home";
import 'semantic-ui-css/semantic.min.css'
import AlbumContent from "./components/AlbumContent";
import {Icon, Menu, Segment} from "semantic-ui-react";

class App extends React.Component {
  render() {
    return (
      <React.Fragment>
        <BrowserRouter>
          {/*Menu header*/}
          <Segment inverted attached>
            <Menu inverted borderless attached>
              {/*<Menu.Item header><Icon name='photo' size='large'/></Menu.Item>*/}
              <Menu.Item as={Link} to='/albums'>
                <Icon name='photo' size='large'/>Home
              </Menu.Item>
            </Menu>
          </Segment>

          {/*Path to Component mapping*/}
          <Switch>
            <Route exact path={"/"} component={Home}/>
            <Route exact path={"/albums"} component={Home}/>
            <Route exact path={"/albums/:name"} component={AlbumContent}/>
          </Switch>
        </BrowserRouter>
      </React.Fragment>
    );
  }
}

export default App;
