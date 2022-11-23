export type AuthenticationContextype = {
  token: string;
  setToken: (token: string) => void;
  role: string;
  setRole: (role: string) => void;
  id: number;
  setId: (id: number) => void;
  fetchdataAuth: (
    url: string,
    method: "GET" | "POST" | "PUT" | "DELETE",
    body: BodyInit,
    params: { [key: string]: any }
  ) => Promise<any>;
  saveToken: (token: string) => void;
  logout: () => void;
};
