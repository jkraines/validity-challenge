import React, { Component } from 'react';
import { getDuplicatesMessage } from "../actions/duplicatesAction";

class Duplicates extends Component {
  constructor(props) {
    super(props);
    this.state = {
      message: 'No message from server'
    };
  }

  componentDidMount() {
    this._isMounted = true;
    getDuplicatesMessage().then(message => {
      if (this._isMounted)
        this.setState({message});
    }).catch(() => {
      if (this._isMounted)
        this.setState({message: 'No response from the server...'});
    });
  }

  componentWillUnmount() {
    this._isMounted = false;
  }

  render() {
    return (
      <div>{this.state.message}</div>
    );
  }
}

export default Duplicates;