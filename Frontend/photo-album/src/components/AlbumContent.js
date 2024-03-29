import React from "react";
import {Button, Card, Icon, Image, Modal} from "semantic-ui-react";

import {deletePhotoById, getAllPhotosByAlbum, getAllPhotosByAlbumFilterAndSort, getPhotoById} from "../api/photosApi";

import PhotoUploader from "./PhotoUploader";
import FilterAndSortModal from "./FilterAndSortModal";

class AlbumContent extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      albumId: this.props.location.state ? this.props.location.state.albumId : null,
      photos: [],
      modalOpen: false,
      viewImage: {
        id: undefined,
        name: undefined,
        url: undefined,
      },
    };

    this.getAllPhotosByAlbum = this.getAllPhotosByAlbum.bind(this);
    this.getAllPhotosByAlbumFilterAndSort = this.getAllPhotosByAlbumFilterAndSort.bind(this);
  }

  componentDidMount() {
    this.getAllPhotosByAlbum();
  }

  getAllPhotosByAlbum() {
    if (this.state.albumId !== null) {
      getAllPhotosByAlbum(this.state.albumId).then((data) => {
        if (data) {
          this.setState({photos: data});
        }
      });
    }
  }

  getAllPhotosByAlbumFilterAndSort(from, to, ascending) {
    getAllPhotosByAlbumFilterAndSort(this.state.albumId, from, to, ascending).then((data) => {
      if (data) {
        this.setState({photos: data});
      }
    });
  }

  handleOpen(name, id) {
    getPhotoById(id).then((data) => {
      if (data) {
        const reader = new FileReader();
        reader.readAsDataURL(data);
        reader.onloadend = () => {
          var base64data = reader.result;
          this.setState({
            viewImage: {
              id: id,
              name: name,
              url: base64data,
            },
          });
        };
      }
    });

    this.setState({modalOpen: true});
  }

  handleClose() {
    this.setState({modalOpen: false, viewImage: {}});
  }

  download() {
    var link = document.createElement("a");
    link.download = this.state.viewImage.name;
    link.href = this.state.viewImage.url;
    link.click();
  }

  deletePhoto() {
    deletePhotoById(this.state.viewImage.id).then((status) => {
      if (status === 200) {
        this.handleClose();
        this.getAllPhotosByAlbum();
      }
    });
  }

  render() {
    return (
      this.state.albumId !== null &&
      <div>
        <PhotoUploader albumId={this.state.albumId} refreshHandler={this.getAllPhotosByAlbum}/>
        <FilterAndSortModal albumId={this.state.albumId} refreshHandler={this.getAllPhotosByAlbumFilterAndSort}
                            resetHandler={this.getAllPhotosByAlbum}/>
        <div className="container-album">
          <Card.Group itemsPerRow={6}>
            {this.state.photos.map((photo) => {
              var base64data = photo.content.toString("base64");
              return (
                <div key={photo.id}>
                  <Modal
                    basic
                    onClose={() => {
                      this.handleClose();
                    }}
                    onOpen={() => {
                      this.handleOpen(photo.name, photo.id);
                    }}
                    open={this.state.modalOpen}
                    size="small"
                    trigger={
                      <Card>
                        <Image
                          src={"data:image/jpg;base64," + base64data}
                          wrapped
                          ui={false}
                        />
                        <Card.Content>
                          <Card.Header>{photo.name}</Card.Header>
                          <Card.Meta>
                            {photo.date}
                          </Card.Meta>
                        </Card.Content>
                      </Card>
                    }
                  >
                    <Modal.Content image>
                      <Image
                        src={this.state.viewImage.url}
                        size="huge"
                        centered
                      />
                    </Modal.Content>
                    <Modal.Actions>
                      <Button
                        color="grey"
                        inverted
                        onClick={() => {
                          this.deletePhoto();
                        }}
                      >
                        <Icon name="trash"/> Delete
                      </Button>
                      <Button
                        color="green"
                        inverted
                        onClick={() => {
                          this.download();
                        }}
                      >
                        <Icon name="download"/> Download
                      </Button>
                    </Modal.Actions>
                  </Modal>
                </div>
              );
            })}
          </Card.Group>
        </div>
      </div>
    );
  }
}

export default AlbumContent;
