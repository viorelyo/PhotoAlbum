const BASE_PATH = "http://localhost:8080/photoAlbum/photos";

export const getPhotoById = (id) => {
  const url = new URL(BASE_PATH + "/download");
  url.search = new URLSearchParams({
    photoId: id,
  });

  return fetch(url).then((response) => response.blob());
};
