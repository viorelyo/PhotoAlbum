const BASE_PATH = "http://localhost:8080/photoAlbum/photos";

export const getPhotoById = (id) => {
  const url = new URL(BASE_PATH + "/download");
  url.search = new URLSearchParams({
    photoId: id,
  });

  return fetch(url).then((response) => response.blob());
};

export const uploadPhoto = (file, albumId) => {
  const url = new URL(BASE_PATH + "/upload");

  const formData = new FormData();
  formData.append("file", file);
  formData.append("albumId", albumId);

  return fetch(url, {
    method: "POST",
    body: formData,
  }).then((response) => response.status);
};
