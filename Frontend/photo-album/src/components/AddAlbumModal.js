import React from "react";
import {Button, Form, Message, Modal} from "semantic-ui-react";
import {addAlbum} from "../api/albumsApi";

class AddAlbumModal extends React.Component {
  state = {
    modalOpen: false,
    albumName: "",
    error: false,
    errorStatus: null
  };

  handleOpen = () => this.setState({modalOpen: true});

  handleClose = () => this.setState({modalOpen: false, albumName: "", error: false, errorStatus: null});

  handleChange = (e) => this.setState({[e.target.name]: e.target.value});

  handleSubmit = (e) => {
    e.preventDefault();
    addAlbum(this.state.albumName).then((status) => {
      if (status !== 201) { // http status CREATED
        this.setState({error: true, errorStatus: status});
      } else {
        this.handleClose();
        this.props.refreshHandler();
      }
    });
  };

  render() {
    return (
      <React.Fragment>
        <Modal
          open={this.state.modalOpen}
          onClose={this.handleClose}
          onOpen={this.handleOpen}
          trigger={<Button>Add album</Button>}
        >
          <Modal.Header>New album</Modal.Header>
          <Modal.Content>
            <Form>
              <Form.Field>
                <input type="text"
                       name="albumName"
                       placeholder="Enter name"
                       value={this.state.albumName}
                       onChange={this.handleChange}
                >
                </input>
              </Form.Field>
            </Form>
            {this.state.error && <Message
              error
              content={this.state.errorStatus === 400 ? 'This name is not allowed' : 'This album already exists'}
            />}
          </Modal.Content>
          <Modal.Actions>
            <Button color='black' onClick={this.handleClose}>
              Cancel
            </Button>
            <Button
              content="Create"
              labelPosition='right'
              icon='checkmark'
              onClick={(e) => this.handleSubmit(e)}
              positive
            />
          </Modal.Actions>
        </Modal>
      </React.Fragment>
    )
  }
}

export default AddAlbumModal;