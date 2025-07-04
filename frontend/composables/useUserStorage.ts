import type { User } from '~/types/user'

export function useUserStorage() {
  const user = useLocalStorage('user', null, {
    serializer: {
      read: (value) =>
        value ? JSON.parse(value) : null,
      write: (value) =>
        JSON.stringify(value),
    }
  })

  function saveUser(data: User) {
    user.value = data
  }

  function removeUser() {
    user.value = null
  }

  return { user, saveUser, removeUser }
}