import axios from 'axios';

// make the api request to get the duplicates
export async function getDuplicatesMessage() {
  return (await axios.get('/api/duplicates')).data;
}