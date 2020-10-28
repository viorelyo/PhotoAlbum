import React from "react";
import {getAllAlbums} from "../api/albumsApi";
import {Container, List} from "semantic-ui-react";
import {Link} from "react-router-dom";

class Home extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      albums: []
    }
  }

  // get albums from server on page load
  componentDidMount() {
    getAllAlbums().then(data => {
      if (data) {
        this.setState({albums: data})
      }
    })
  }

  render() {
    return (
      <React.Fragment>
        <Container>
          <List divided relaxed size='big'>
            {
              this.state.albums.map(album => (
                <List.Item key={album.id} as={Link} to={'/albums/' + album.name}>
                  <List.Icon name='folder' size='large'/>
                  <List.Content>
                    <List.Header>{album.name}</List.Header>
                  </List.Content>
                </List.Item>
              ))
            }
          </List>
        </Container>
      </React.Fragment>
    )
  }
}

export default Home;