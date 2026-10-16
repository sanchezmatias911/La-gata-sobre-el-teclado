export type ClassType = 'Funcional' | 'Fitcombat' | 'Zumba'
                      | 'Yoga' | 'Stretching' | 'Full local'
                      | 'Gap'  | 'Libre';

export type StatusType = 'Activa' | 'Cancelada';

export interface ScheduleCell {
  time: string;
  activity: ClassType | null;
  status?: StatusType;
}

export const DAYS = ['Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'];

export const SCHEDULE_DATA: ScheduleCell[][] = [
  [
    { time: '08:00', activity: 'Funcional', status: 'Cancelada' }, { time: '08:00', activity: 'Funcional', status: 'Cancelada' },
    { time: '08:00', activity: 'Funcional', status: 'Cancelada' }, { time: '08:00', activity: 'Funcional', status: 'Cancelada' },
    { time: '08:00', activity: 'Funcional', status: 'Cancelada' }, { time: '08:00', activity: null }
  ],
  [
    { time: '09:00', activity: 'Funcional', status: 'Cancelada' }, { time: '09:00', activity: 'Funcional', status: 'Cancelada' },
    { time: '09:00', activity: 'Funcional', status: 'Cancelada' }, { time: '09:00', activity: 'Funcional', status: 'Activa' },
    { time: '09:00', activity: 'Funcional', status: 'Activa' }, { time: '09:00', activity: null }
  ],
  [
    { time: '10:00', activity: 'Fitcombat', status: 'Activa' }, { time: '10:00', activity: 'Zumba', status: 'Cancelada' },
    { time: '10:00', activity: 'Fitcombat', status: 'Activa' }, { time: '10:00', activity: 'Zumba', status: 'Activa' },
    { time: '10:00', activity: 'Fitcombat', status: 'Activa' }, { time: '10:00', activity: 'Fitcombat', status: 'Cancelada' }
  ],
  [
    { time: '11:00', activity: 'Funcional', status: 'Activa' }, { time: '11:00', activity: 'Funcional', status: 'Activa' },
    { time: '11:00', activity: 'Funcional', status: 'Activa' }, { time: '11:00', activity: 'Funcional', status: 'Activa' },
    { time: '11:00', activity: 'Funcional', status: 'Activa' }, { time: '11:00', activity: 'Funcional', status: 'Cancelada' }
  ],
  [
    { time: '12:00', activity: 'Yoga', status: 'Activa' }, { time: '12:00', activity: 'Stretching', status: 'Cancelada' },
    { time: '12:00', activity: 'Yoga', status: 'Activa' }, { time: '12:00', activity: 'Stretching', status: 'Activa' },
    { time: '12:00', activity: 'Yoga', status: 'Activa' }, { time: '12:00', activity: 'Zumba', status: 'Activa' }
  ],
  [
    { time: '16:00', activity: 'Funcional', status: 'Activa' }, { time: '16:00', activity: 'Funcional', status: 'Activa' },
    { time: '16:00', activity: 'Funcional', status: 'Cancelada' }, { time: '16:00', activity: 'Funcional', status: 'Activa' },
    { time: '16:00', activity: 'Funcional', status: 'Activa' }, { time: '16:00', activity: 'Funcional', status: 'Cancelada' }
  ],
  [
    { time: '17:00', activity: 'Full local', status: 'Activa' }, { time: '17:00', activity: 'Zumba', status: 'Activa' },
    { time: '17:00', activity: 'Full local', status: 'Activa' }, { time: '17:00', activity: 'Zumba', status: 'Activa' },
    { time: '17:00', activity: 'Full local', status: 'Cancelada' }, { time: '17:00', activity: null }
  ],
  [
    { time: '18:00', activity: 'Funcional', status: 'Activa' }, { time: '18:00', activity: 'Funcional', status: 'Activa' },
    { time: '18:00', activity: 'Funcional', status: 'Activa' }, { time: '18:00', activity: 'Funcional', status: 'Activa' },
    { time: '18:00', activity: 'Funcional', status: 'Activa' }, { time: '18:00', activity: null }
  ],
  [
    { time: '19:00', activity: 'Gap', status: 'Activa' }, { time: '19:00', activity: 'Full local', status: 'Activa' },
    { time: '19:00', activity: 'Gap', status: 'Cancelada' }, { time: '19:00', activity: 'Full local', status: 'Activa' },
    { time: '19:00', activity: 'Gap', status: 'Cancelada' }, { time: '19:00', activity: null }
  ],
  [
    { time: '20:00', activity: 'Funcional', status: 'Activa' }, { time: '20:00', activity: 'Fitcombat', status: 'Activa' },
    { time: '20:00', activity: 'Funcional', status: 'Activa' }, { time: '20:00', activity: 'Fitcombat', status: 'Activa' },
    { time: '20:00', activity: 'Funcional', status: 'Activa' }, { time: '20:00', activity: null }
  ],
  [
    { time: '21:00', activity: 'Stretching', status: 'Activa' }, { time: '21:00', activity: 'Funcional', status: 'Activa' },
    { time: '21:00', activity: 'Stretching', status: 'Activa' }, { time: '21:00', activity: 'Funcional', status: 'Cancelada' },
    { time: '21:00', activity: 'Stretching', status: 'Activa' }, { time: '21:00', activity: null }
  ]
];