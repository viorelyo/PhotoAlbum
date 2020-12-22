import React from "react";
import { Button, Icon, Modal, Image } from "semantic-ui-react";

import { getPhotoById } from "../api/photosApi";
import {getAllPhotosBinariesByAlbum} from "../api/photosApi"

import PhotoUploader from "./PhotoUploader";
import { getAllPhotosByAlbum } from "../api/photosApi";
import { Container, List } from "semantic-ui-react";
import { Link } from "react-router-dom";
import "./Images.css"

class AlbumContent extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      modalOpen: false,
      viewImage: {
        name: undefined,
        url: undefined,
      },
      photos: [{}],
      albumId: this.props.match.params.id,
      photoBinaries: [{}],
      index: 0
    };
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
              name: name,
              url: base64data,
            },
          });
        };
      }
    });

    this.setState({ modalOpen: true });
  }

  handleClose() {
    this.setState({ modalOpen: false, viewImage: {} });
  }

  download() {
    var link = document.createElement("a");
    link.download = this.state.viewImage.name;
    link.href = this.state.viewImage.url;
    link.click();
  }

  // get photos from server on page load
  componentDidMount() {
    getAllPhotosByAlbum(this.state.albumId).then(data => {
      if (data) {
        this.setState({ photos: data })
      }
    })
    getAllPhotosBinariesByAlbum(this.state.albumId).then(data => {
      if (data) {
        this.setState({ photoBinaries: data })
      }
    })
  }

//<img className='singleImage' src={process.env.PUBLIC_URL + '/' + photo.filePath} alt={photo.name} />
//<img className='singleImage' src={this.state.photoBinaries[photo.id]} size="huge" alt={photo.name} />

  render() {
    return (

      <div>
        <PhotoUploader />
        {this.state.photos.map(photo => (
          <div>
            {console.log(this.state.photoBinaries[0])}
            
            <Modal
              basic
              onClose={() => {
                this.handleClose();
              }}
              onOpen={() => {
                this.handleOpen(photo.name, photo.albumId);
                console.log(photo.name);
              }}
              open={this.state.modalOpen}
              size="small"
              trigger={<Button>Basic Modal</Button>}
            >
              <Modal.Content image>
                <Image src={this.state.viewImage.url} size="huge" centered />
              </Modal.Content>
              <Modal.Actions>
                <Button
                  color="green"
                  inverted
                  onClick={() => {
                    this.download();
                  }}
                >
                  <Icon name="download" /> Download
            </Button>
              </Modal.Actions>
            </Modal>
          </div>
        ))
        }
      </div>
    );
  }
}

export default AlbumContent;
