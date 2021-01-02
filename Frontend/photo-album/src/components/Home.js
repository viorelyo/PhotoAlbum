import React from "react";
import {getAllAlbums} from "../api/albumsApi";
import {Container, List} from "semantic-ui-react";
import {Link} from "react-router-dom";
import AddAlbumModal from "./AddAlbumModal";


class Home extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      albums: []
    }

    this.getAllAlbums = this.getAllAlbums.bind(this);
  }

  // get albums from server on page load
  componentDidMount() {
    this.getAllAlbums();
  }

  getAllAlbums() {
    getAllAlbums().then(data => {
      if (data) {
        this.setState({albums: data})
      }
    })
  }

  handleClick(albumId) {
    this.setState({
      albumId: albumId
    });
  }

  // onClick={this.handleClick(album.id)}
  // as={Link} to={'/albums/' + album.name} state={this.state.albumId = album.id}

  render() {
    return (
      <React.Fragment>
        <Container>
          <AddAlbumModal refreshHandler={this.getAllAlbums}/>
          <List divided relaxed size='big'>
            {
              this.state.albums.map(album => (
                <List.Item key={album.id} as={Link} to={{
                  pathname: '/albums/' + album.name,
                  state: {
                    albumId: album.id
                  }
                }} value={album.id}>
                  <List.Icon name='folder' size='large'/>
                  <List.Content>
                    <List.Header onClick={this.handleClick.bind(this, album.id)}>{album.name}</List.Header>
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