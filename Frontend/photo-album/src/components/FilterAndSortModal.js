import React from "react";
import {Button, Form, Modal, Radio} from "semantic-ui-react";

class FilterAndSortModal extends React.Component {
  defaultFrom = '2000-01-01';
  defaultTo = new Date(Date.now()).toISOString().split('T')[0];

  state = {
    modalOpen: false,
    sort: null,
    filter: false,
    from: this.defaultFrom,
    to: this.defaultTo,
  };

  handleOpen = () => this.setState({modalOpen: true});

  handleClose = () => this.setState({modalOpen: false});

  handleChangeSortMethod = (e, {value}) => this.setState({sort: value});

  handleChangeFilterMethod = (e, {value}) => this.setState({filter: value});

  handleDateChange = (e) => this.setState({[e.target.name]: e.target.value});

  handleReset = () => {
    this.setState({modalOpen: false, sort: null, filter: false, from: this.defaultFrom, to: this.defaultTo});
    this.props.resetHandler();
  }

  handleSubmit = (e) => {
    e.preventDefault();
    var fromParam = null;
    var toParam = null;
    if (this.state.filter) {
      fromParam = this.state.from;
      toParam = this.state.to;
    }
    this.props.refreshHandler(fromParam, toParam, this.state.sort);
    this.handleClose();
  };

  render() {
    return (
      <React.Fragment>
        <Modal
          closeIcon
          open={this.state.modalOpen}
          onClose={this.handleClose}
          onOpen={this.handleOpen}
          trigger={<Button style={{'margin-left': '24px'}}>Sort & Filter</Button>}
        >
          <Modal.Header>Add sort & filter parameters</Modal.Header>
          <Modal.Content>
            <Form>
              <Form.Group>
                <label>Sort: </label>
                <Form.Field
                  control={Radio}
                  label='None'
                  value={null}
                  checked={this.state.sort === null}
                  onChange={this.handleChangeSortMethod}
                />
                <Form.Field
                  control={Radio}
                  label='Ascending by date'
                  value={true}
                  checked={this.state.sort === true}
                  onChange={this.handleChangeSortMethod}
                />
                <Form.Field
                  control={Radio}
                  label='Descending by date'
                  value={false}
                  checked={this.state.sort === false}
                  onChange={this.handleChangeSortMethod}
                />
              </Form.Group>

              <Form.Group>
                <label>Date uploaded: </label>
                <Form.Field
                  control={Radio}
                  label='Any time'
                  value={false}
                  checked={this.state.filter === false}
                  onChange={this.handleChangeFilterMethod}
                />
                <Form.Field
                  control={Radio}
                  label='Custom'
                  value={true}
                  checked={this.state.filter === true}
                  onChange={this.handleChangeFilterMethod}
                />
              </Form.Group>
              {this.state.filter && <Form.Group>
                <Form.Field>
                  <input type="date"
                         name="from"
                         max={this.state.to}
                         label="From:"
                         value={this.state.from}
                         onChange={this.handleDateChange}
                  >
                  </input>
                </Form.Field>
                <Form.Field>
                  <input type="date"
                         name="to"
                         min={this.state.from}
                         label="To:"
                         value={this.state.to}
                         onChange={this.handleDateChange}
                  >
                  </input>
                </Form.Field>
              </Form.Group>}
            </Form>
          </Modal.Content>
          <Modal.Actions>
            <Button color='black' onClick={this.handleReset}>
              Reset
            </Button>
            <Button
              content="Apply"
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

export default FilterAndSortModal;