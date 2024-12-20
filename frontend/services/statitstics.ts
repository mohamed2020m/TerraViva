
const API_URL = `${process.env.NEXT_PUBLIC_BACKEND_API}/api/v1`


export const fetchProfessorCount = async (accessToken : string) => {
    try {
        
      //console.log("API_URL : ", API_URL);  
      const response = await fetch(`${API_URL}/professors/count`, {
        method: 'GET',
        headers: {
          Authorization: `Bearer ${accessToken}`,
          'Content-Type': 'application/json',
        },
      });
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      const data = await response.json();
      return data;
    } catch (error) {
      console.error('Failed to fetch professor count:', error);
      throw error;
    }
  };

  export const fetchObjectsCount = async (accessToken : string, professorId : number) => {
    try {
        
      const response = await fetch(`${API_URL}/threeDObjects/count-by/${professorId}`, {
        method: 'GET',
        headers: {
          Authorization: `Bearer ${accessToken}`,
          'Content-Type': 'application/json',
        },
      });
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      const data = await response.json();
      return data;
    } catch (error) {
      console.error('Failed to fetch professor count:', error);
      throw error;
    }
  };

  export const fetchMainCategoryCount = async (accessToken : string) => {
    try {
        const response = await fetch(`${API_URL}/categories/main/count`, {
        method: 'GET',
        headers: {
          Authorization: `Bearer ${accessToken}`,
          'Content-Type': 'application/json',
        },
      });
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      const data = await response.json();
      return data;
    } catch (error) {
      console.error('Failed to fetch professor count:', error);
      throw error;
    }
  };

  export const fetchStudentsByMainCategories = async (accessToken : string) => {
    try {
        const response = await fetch(`${API_URL}/students/main-categories/student-counts`, {
        method: 'GET',
        headers: {
          Authorization: `Bearer ${accessToken}`,
          'Content-Type': 'application/json',
        },
      });
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      const data = await response.json();
      return data;
    } catch (error) {
      console.error('Failed to fetch professor count:', error);
      throw error;
    }
  };

  export const fetchSubCategoryCount = async (accessToken : string, professorId: number) => {
    try {
        const response = await fetch(`${API_URL}/professors/${professorId}/sub-categories/count`, {
        method: 'GET',
        headers: {
          Authorization: `Bearer ${accessToken}`,
          'Content-Type': 'application/json',
        },
      });
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      const data = await response.json();
      return data;
    } catch (error) {
      console.error('Failed to fetch professor count:', error);
      throw error;
    }
  };

  export const fetchSubCategoriesByStudents = async (accessToken : string, professorId: number) => {
    try {
        const response = await fetch(`${API_URL}/professors/${professorId}/sub-categories/student-counts`, {
        method: 'GET',
        headers: {
          Authorization: `Bearer ${accessToken}`,
          'Content-Type': 'application/json',
        },
      });
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      const data = await response.json();
      return data;
    } catch (error) {
      console.error('Failed to fetch professor count:', error);
      throw error;
    }
  };

  export const fetchObjectsBySubCategories = async (accessToken : string, professorId: number) => {
    try {
        const response = await fetch(`${API_URL}/threeDObjects/${professorId}/by-categories`, {
        method: 'GET',
        headers: {
          Authorization: `Bearer ${accessToken}`,
          'Content-Type': 'application/json',
        },
      });
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      const data = await response.json();
      return data;
    } catch (error) {
      console.error('Failed to fetch professor count:', error);
      throw error;
    }
  };

  export const fetchStudentsCount = async (accessToken : string) => {
    try {
        const response = await fetch(`${API_URL}/students/count`, {
        method: 'GET',
        headers: {
          Authorization: `Bearer ${accessToken}`,
          'Content-Type': 'application/json',
        },
      });
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      const data = await response.json();
      return data;
    } catch (error) {
      console.error('Failed to fetch professor count:', error);
      throw error;
    }
  };

  export const fetchAdministratorsCount = async (accessToken : string) => {
    try {
        const response = await fetch(`${API_URL}/administrators/count`, {
        method: 'GET',
        headers: {
          Authorization: `Bearer ${accessToken}`,
          'Content-Type': 'application/json',
        },
      });
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      const data = await response.json();
      return data;
    } catch (error) {
      console.error('Failed to fetch professor count:', error);
      throw error;
    }
  };

  export const fetchProfessorsByCategories = async (accessToken : string) => {
    try {
        const response = await fetch(`${API_URL}/professors/by-categories`, {
        method: 'GET',
        headers: {
          Authorization: `Bearer ${accessToken}`,
          'Content-Type': 'application/json',
        },
      });
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      const data: [string, number][] = await response.json();
      // console.log("Data for Pie Chart : ", data); 
      return data;
    } catch (error) {
      console.error('Failed to fetch professor count:', error);
      throw error;
    }
  };