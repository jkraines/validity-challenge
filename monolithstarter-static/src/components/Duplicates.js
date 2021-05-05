import React, { Component } from 'react';
import { getDuplicatesMessage } from "../actions/duplicatesAction";

class Duplicates extends Component {
  constructor(props) {
    console.log("CONSTRUCTOR");
    super(props);
    this.state = {
        duplicates: [],
        nonduplicates: []
    };
  }

  componentDidMount() {
    console.log("MOUNTING");
    this._isMounted = true;
    getDuplicatesMessage().then(response => {
      console.log(response);
      if (this._isMounted)
        this.setState({
            duplicates: response.duplicates
        });
        this.setState({
            nonduplicates: response.nonduplicates
        });
    }).catch(() => {
      if (this._isMounted)
        console.log("Could not connect to server!");
    });
  }

  componentWillUnmount() {
    this._isMounted = false;
  }

  render() {
    return (
        <div>
            <h1>Duplicates</h1>
            <pre>{JSON.stringify(this.state.duplicates, null, 2)}</pre>
            <h1>Non-Duplicates</h1>
            <pre>{JSON.stringify(this.state.nonduplicates, null, 2)}</pre>
        </div>
    );
  }
}

export default Duplicates;