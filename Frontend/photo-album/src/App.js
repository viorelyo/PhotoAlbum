import React from "react";
import {BrowserRouter, Link, Route, Switch} from "react-router-dom";
import Home from "./components/Home";
import 'semantic-ui-css/semantic.min.css'
import AlbumContent from "./components/AlbumContent";
import {Menu, Segment} from "semantic-ui-react";

class App extends React.Component {
  render() {
    return (
      <React.Fragment>
        <BrowserRouter>
          {/*Menu header*/}
          <Segment inverted attached>
            <Menu inverted borderless attached size='large'>
              <Menu.Item header>Photo Album</Menu.Item>
              <Menu.Item as={Link} to='/albums' name='home'/>
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
