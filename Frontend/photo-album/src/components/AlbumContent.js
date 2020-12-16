import React from "react";
import { Button, Icon, Modal, Image } from "semantic-ui-react";

import { getPhotoById } from "../api/photosApi";

import PhotoUploader from "./PhotoUploader";

class AlbumContent extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      modalOpen: false,
      viewImage: {
        name: undefined,
        url: undefined,
      },
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

  render() {
    return (
      <div>
        <PhotoUploader />
        <Modal
          basic
          onClose={() => {
            this.handleClose();
          }}
          onOpen={() => {
            this.handleOpen("test.jpg", 1);
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
    );
  }
}

export default AlbumContent;
