export const paramsMaker = (object: { [key: string]: any }) => {
  let params = "?";
  for (const item in object) {
    params += item + "=" + object[item] + "&";
  }
  params.slice(0, params.length - 2);
  return params;
};
