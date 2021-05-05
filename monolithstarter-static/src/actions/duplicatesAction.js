import axios from 'axios';

export async function getDuplicatesMessage() {
  return (await axios.get('/api/duplicates')).data;
}