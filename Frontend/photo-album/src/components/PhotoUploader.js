import React from "react";
import Dropzone from "react-dropzone";
import { Icon, Segment } from "semantic-ui-react";

import { uploadPhoto } from "../api/photosApi";

class PhotoUploader extends React.PureComponent {
  constructor(props) {
    super(props);
    this.state = {
      files: [],
    };

    this.fileInputCallback = this.fileInputCallback.bind(this);
  }

  fileInputCallback(acceptedFiles) {
    let albumId = this.props.albumId;

    acceptedFiles.forEach((file) => {
      uploadPhoto(file, albumId).then((status) => {
        //TODO can be shown on page
        if (status === 200) {
          this.setState({ files: [...this.state.files, file] });
          this.props.refreshHandler();
        }
      });
    });
  }

  render() {
    return (
      <Dropzone accept="image/*" onDrop={this.fileInputCallback}>
        {({ getRootProps, getInputProps }) => (
          <Segment
            inverted
            tertiary
            color="black"
            textAlign="center"
            {...getRootProps()}
          >
            <input {...getInputProps()} />
            <Icon name="cloud upload" size="big" />
            <div>Drag and drop or upload file</div>
          </Segment>
        )}
      </Dropzone>
    );
  }
}

export default PhotoUploader;
