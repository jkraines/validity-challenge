import React, { Component } from 'react';
import { Container } from 'reactstrap';
import Duplicates from '../components/Duplicates';

class DuplicatesPage extends Component {
  render() {
    return (
      <div className='app'>
        <div className='app-body'>
          <Container fluid className='text-center'>
            <Duplicates />
          </Container>
        </div>
      </div>
    );
  }
}

export default DuplicatesPage;