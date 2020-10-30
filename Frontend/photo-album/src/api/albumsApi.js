
const BASE_PATH = "http://localhost:8080/photoAlbum/albums";

export const getAllAlbums = () => {
  const url = new URL(BASE_PATH);

  return fetch(url.toString())
    .then(response => response.json());
}
