import React from "react";
import Dropzone from "react-dropzone";
import {Icon, Message, MessageContent, Segment} from "semantic-ui-react";

import {uploadPhoto} from "../api/photosApi";

class PhotoUploader extends React.PureComponent {
  constructor(props) {
    super(props);
    this.state = {
      files: [],
      error: false,
      errorMessage: "",
    };

    this.fileInputCallback = this.fileInputCallback.bind(this);
    this.handleDismissError = this.handleDismissError.bind(this);
  }

  fileInputCallback(acceptedFiles) {
    let albumId = this.props.albumId;

    acceptedFiles.forEach((file) => {
      uploadPhoto(file, albumId).then((status) => {
        //TODO can be shown on page
        if (status === 200) {
          this.setState({files: [...this.state.files, file]});
          this.props.refreshHandler();
        } else {
          this.setState({error: true, errorMessage: "Please try again"});
          setTimeout(() => {
            this.setState({error: false, errorMessage: ""})
          }, 5000)
        }
      }).catch(e => {
        this.setState({error: true, errorMessage: "File size is too big"})
        setTimeout(() => {
          this.setState({error: false, errorMessage: ""})
        }, 5000)
      });
    });
  }

  handleDismissError() {
    this.setState({error: false, errorMessage: ""})
  }

  render() {
    return (
      <React.Fragment>
        <Dropzone accept="image/*" onDrop={this.fileInputCallback}>
          {({getRootProps, getInputProps}) => (
            <Segment
              inverted
              tertiary
              color="black"
              textAlign="center"
              {...getRootProps()}
            >
              <input {...getInputProps()} />
              <Icon name="cloud upload" size="big"/>
              <div>Drag and drop or upload file</div>
            </Segment>
          )}
        </Dropzone>
        {this.state.error &&
        <Message negative onDismiss={this.handleDismissError}>
          <Message.Header>Upload failed</Message.Header>
          <MessageContent>{this.state.errorMessage}</MessageContent>
        </Message>}
      </React.Fragment>
    );
  }
}

export default PhotoUploader;
