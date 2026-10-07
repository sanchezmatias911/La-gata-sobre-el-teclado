
export type ClassType = 'Funcional' | 'Fitcombat' | 'Zumba' 
                      | 'Yoga' | 'Stretching' | 'Full local' 
                      | 'Gap' | 'Libre';

export interface ScheduleCell {
  time: string;      
  activity: ClassType | null;
}

export const DAYS = ['Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'];



export const SCHEDULE_DATA: ScheduleCell[][] = [
  // 08:00
  [
    { time: '08:00', activity: 'Funcional' }, { time: '08:00', activity: 'Funcional' }, 
    { time: '08:00', activity: 'Funcional' }, { time: '08:00', activity: 'Funcional' }, 
    { time: '08:00', activity: 'Funcional' }, { time: '08:00', activity: null }
  ],
  // 09:00
  [
    { time: '09:00', activity: 'Funcional' }, { time: '09:00', activity: 'Funcional' }, 
    { time: '09:00', activity: 'Funcional' }, { time: '09:00', activity: 'Funcional' }, 
    { time: '09:00', activity: 'Funcional' }, { time: '09:00', activity: null }
  ],
  // 10:00
  [
    { time: '10:00', activity: 'Fitcombat' }, { time: '10:00', activity: 'Zumba' }, 
    { time: '10:00', activity: 'Fitcombat' }, { time: '10:00', activity: 'Zumba' }, 
    { time: '10:00', activity: 'Fitcombat' }, { time: '10:00', activity: 'Fitcombat' }
  ],
  // 11:00
  [
    { time: '11:00', activity: 'Funcional' }, { time: '11:00', activity: 'Funcional' }, 
    { time: '11:00', activity: 'Funcional' }, { time: '11:00', activity: 'Funcional' }, 
    { time: '11:00', activity: 'Funcional' }, { time: '11:00', activity: 'Funcional' }
  ],
  // 12:00
  [
    { time: '12:00', activity: 'Yoga' }, { time: '12:00', activity: 'Stretching' }, 
    { time: '12:00', activity: 'Yoga' }, { time: '12:00', activity: 'Stretching' }, 
    { time: '12:00', activity: 'Yoga' }, { time: '12:00', activity: 'Zumba' }
  ],

];